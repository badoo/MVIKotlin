// The ID of the extension we want to talk to.
var editorExtensionId = "ekffenjemdhlffjmedmmndlmhfgmcadj";

var port = chrome.runtime.connect(editorExtensionId)

console.log(port)

port.onMessage.addListener(function(msg) {
  console.log("onMessage")
  console.log(msg)
});

port.postMessage("Heh!")

