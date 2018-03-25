<#-- @ftlvariable name="model" type="de.mkammerer.pocketknife.pocketknife.json.JsonPrettifier.Model" -->
<#include '../include/header.ftlh'>

<h1>JSON prettify / minify</h1>

<#if model.error??>
<div class="alert alert-danger" role="alert">
    Decoding failed: ${model.error}
</div>
</#if>

<h2>Prettify</h2>

<form name="form" method="post" action="/json/prettifier/prettify">
    <div class="form-group">
        <label for="ugly">Ugly JSON</label>
        <textarea class="code form-control" id="ugly" name="ugly" rows="10">${model.ugly}</textarea>
    </div>
    <button type="submit" class="btn btn-success">Prettify</button>
</form>

<h2>Minify</h2>

<form name="form" method="post" action="/json/prettifier/minify">
    <div class="form-group">
        <label for="pretty">Pretty JSON</label>
        <textarea class="code form-control" id="pretty" name="pretty" rows="10">${model.pretty}</textarea>
    </div>
    <button type="submit" class="btn btn-success">Minify</button>
</form>

<#include '../include/footer.ftlh'>