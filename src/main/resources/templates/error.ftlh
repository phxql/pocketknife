<#-- @ftlvariable name="model" type="de.mkammerer.pocketknife.pocketknife.ErrorHandler.Model" -->

<#include 'include/header.ftlh'>

<h1>Unhandled exception occurred</h1>

<p>
    This shouldn't have happened. Please <a href="https://github.com/phxql/pocketknife/issues/new">submit a bug
    report</a> containing the stacktrace below. Thanks!
</p>

<div class="form-group">
    <label for="stacktrace">Stacktrace for the developers:</label>
    <textarea id="stacktrace" class="code form-control" rows="30" readonly>${model.stacktrace}</textarea>
</div>

<#include 'include/footer.ftlh'>