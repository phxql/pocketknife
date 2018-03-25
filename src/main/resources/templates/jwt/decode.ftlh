<#-- @ftlvariable name="model" type="de.mkammerer.pocketknife.pocketknife.json.JwtDecode.Model" -->
<#include '../include/header.ftlh'>

<h1>JWT decoder</h1>

<#if model.error??>
<div class="alert alert-danger" role="alert">
    Decoding failed: ${model.error}
</div>
</#if>

<form name="form" method="post" action="/jwt/decode/decode">
    <div class="form-group">
        <label for="jwt">JWT</label>
        <input type="text" class="form-control" id="jwt" name="jwt" value="${model.jwt}">
    </div>
    <button type="submit" class="btn btn-success">Decode</button>

    <div class="form-group">
        <label for="header">Header</label>
        <textarea class="code form-control" id="header" rows="5" readonly>${model.header}</textarea>
    </div>
    <div class="form-group">
        <label for="payload">Payload</label>
        <textarea class="code form-control" id="payload" rows="10" readonly>${model.payload}</textarea>
    </div>
</form>

<p>Parsed payload:</p>

<table class="table table-sm table-hover">
    <tbody>
    <#if model.exp??>
<tr>
    <td>Expiration (exp)</td>
    <td>${model.exp}</td>
</tr>
    </#if>

<#if model.nbf??>
<tr>
    <td>Not before (nbf)</td>
    <td>${model.nbf}</td>
</tr>
</#if>

<#if model.iat??>
<tr>
    <td>Issued at (iat)</td>
    <td>${model.iat}</td>
</tr>
</#if>
    </tbody>
</table>

<#include '../include/footer.ftlh'>