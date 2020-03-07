package com.arkivanov.mvikotlin.sample.todo.reaktive.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.sample.todo.reaktive.store.MyStore.*
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.toObservable


interface MyStore : Store<Intent, State, Label> {

    sealed class Intent {
        object Do: Intent()
    }

    class State

    sealed class Label {
        object Done: Label()
    }

}


class MyStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): MyStore =
        object : MyStore, Store<Intent, State, Label> by storeFactory.create(
            name = "asd",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.Init),
            executorFactory = {
                PureExecutor(
                    intentToAction = Action::ExecuteIntent,
                    actor = ActorImpl(),
                    labelPublisher = LabelPublisherImpl(),
                    postProcessor = PostProcessorImpl()
                )
            },
            reducer = ReducerImpl
        ) {
        }

    private sealed class Action {
        data class ExecuteIntent(val intent: Intent): Action()
        object Init: Action()
    }

    private sealed class Result {
        object Done: Result()
    }

    private class ActorImpl : Actor<Action, State, Result> {
        override fun invoke(action: Action, state: State): Observable<Result> =
            when (action) {
                is Action.ExecuteIntent ->
                    when (action.intent) {
                        is Intent.Do -> Result.Done.toObservable()
                    }

                is Action.Init -> Result.Done.toObservable()
            }
    }

    private object ReducerImpl: Reducer<State, Result> {
        override fun State.reduce(result: Result): State {
            TODO("Not yet implemented")
        }
    }

    private class LabelPublisherImpl : LabelPublisher<Result, State, Label> {
        override fun invoke(p1: Result, p2: State): Label? {
            TODO("Not yet implemented")
        }
    }

    private class PostProcessorImpl : PostProcessor<Result, State, Action> {
        override fun invoke(p1: Result, p2: State): Action? {
            TODO("Not yet implemented")
        }
    }
}
