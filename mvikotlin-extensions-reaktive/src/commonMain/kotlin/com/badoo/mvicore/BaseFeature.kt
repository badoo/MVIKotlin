package com.badoo.mvicore

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveBootstrapper
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableObserver
import com.arkivanov.mvikotlin.core.store.Bootstrapper as MviBootstrapper
import com.arkivanov.mvikotlin.core.store.Reducer as MviReducer

typealias Actor<State, Action, Effect> =
        (state: State, action: Action) -> Observable<Effect>

typealias Bootstrapper<Action> = () -> Observable<Action>

typealias NewsPublisher<Action, Effect, State, News> =
        (action: Action, effect: Effect, state: State) -> News?

typealias PostProcessor<Action, Effect, State> =
        (action: Action, effect: Effect, state: State) -> Action?

typealias Reducer<State, Effect> =
        (state: State, effect: Effect) -> State

typealias WishToAction<Wish, Action> = (wish: Wish) -> Action

interface Store<in Wish : Any, out State : Any> : Observable<State> {

    val state: State

    fun accept(wish: Wish)
}

interface Feature<in Wish : Any, out State : Any, out News : Any> : Store<Wish, State>, Disposable {

    val news: Observable<News>
}

open class BaseFeature<in Wish : Any, in Action : Any, Effect : Any, out State : Any, out News : Any>(
    initialState: State,
    bootstrapper: Bootstrapper<Action>? = null,
    private val wishToAction: WishToAction<Wish, Action>,
    private val actor: Actor<State, Action, Effect>,
    reducer: Reducer<State, Effect>,
    private val postProcessor: PostProcessor<Action, Effect, State>? = null,
    private val newsPublisher: NewsPublisher<Action, Effect, State, News>? = null
) : Feature<Wish, State, News> {

    private val delegate =
        storeFactory.create(
            initialState = initialState,
            bootstrapper = bootstrapper?.convert(),
            executorFactory = { MviCoreExecutor(wishToAction, actor, newsPublisher, postProcessor) },
            reducer = reducer.convert()
        )

    override val state: State get() = delegate.state

    override fun accept(wish: Wish) {
        delegate.accept(wish)
    }

    override val news: Observable<News> get() = delegate.labels

    override fun subscribe(observer: ObservableObserver<State>) {
        delegate.states.subscribe(observer)
    }

    override val isDisposed: Boolean get() = delegate.isDisposed

    override fun dispose() {
        delegate.dispose()
    }
}

private fun <Action : Any> Bootstrapper<Action>.convert(): MviBootstrapper<Action> =
    object : ReaktiveBootstrapper<Action>() {
        override fun invoke() {
            this@convert
                .invoke()
                .subscribeScoped(isThreadLocal = true, onNext = ::dispatch)
        }
    }

private fun <State : Any, Effect : Any> Reducer<State, Effect>.convert(): MviReducer<State, Effect> =
    object : com.arkivanov.mvikotlin.core.store.Reducer<State, Effect> {
        override fun State.reduce(effect: Effect): State =
            this@convert.invoke(this, effect)
    }

private class MviCoreExecutor<in Wish : Any, in Action : Any, in State : Any, Effect : Any, News : Any>(
    private val wishToAction: (Wish) -> Action,
    private val actor: Actor<State, Action, Effect>,
    private val newsPublisher: NewsPublisher<Action, Effect, State, News>?,
    private val postProcessor: PostProcessor<Action, Effect, State>?
) : ReaktiveExecutor<Wish, Action, State, Effect, News>() {

    override fun executeAction(action: Action, getState: () -> State) {
        actor(getState(), action).subscribeScoped { result ->
            dispatch(result)
            val newState = getState()
            newsPublisher?.invoke(action, result, newState)?.also(::publish)

            postProcessor?.invoke(action, result, newState)?.also {
                executeAction(it, getState)
            }
        }
    }

    override fun executeIntent(wish: Wish, getState: () -> State) {
        executeAction(wishToAction(wish), getState)
    }
}

lateinit var storeFactory: StoreFactory // Initialize first or use default one
