<#-- @ftlvariable name="model" type="de.mkammerer.pocketknife.pocketknife.encoding.Base64Decoder.Model" -->
<#include '../include/header.ftlh'>

<h1>Base64 encoder / decoder</h1>

<h2>Encode</h2>

<form name="form" method="post" action="/encoding/base64/encode">
    <div class="form-group">
        <label for="plaintext">Plaintext</label>
        <textarea class="code form-control" id="plaintext" name="plaintext" rows="10">${model.plaintext}</textarea>
    </div>
    <button type="submit" class="btn btn-success">Plaintext -> Base64</button>
</form>

<h2>Decode</h2>

<#if model.error??>
<div class="alert alert-danger" role="alert">
    Decoding failed: ${model.error}
</div>
</#if>

<form name="form" method="post" action="/encoding/base64/decode">
    <div class="form-group">
        <label for="base64">Base64</label>
        <textarea class="code form-control" id="base64" name="base64" rows="10">${model.base64}</textarea>
    </div>
    <button type="submit" class="btn btn-success">Base64 -> Plaintext</button>
</form>

<#include '../include/footer.ftlh'>