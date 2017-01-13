<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings/offeroptions"><fmt:message key="OfferOptions"/></a></li>
            <li class="active"><fmt:message key="OfferOption"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="OfferOption"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="relative">
                        <spf:select path="offerOptionType" class="select-simple form-control" data-style="form-top-element" data-container="body">
                            <spf:options items="${OfferOptionTypes}"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="OfferOptionType"/></span>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="name" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Name"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="description" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="cameraUrl" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="CameraUrl"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="cameraPort" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="CameraPort"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="cameraUser" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="CameraUser"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:textarea path="cameraKey" class="form-control form-bottom-element" rows="27" placeholder="
-----BEGIN RSA PRIVATE KEY-----
MIIEowIBAAKCAQEA5EGueSLUqjWpNEYJDcxqfBolvRSRl86x4e2U3Lh5x58hOV5S
vOj4WmsWsuYnv6d0ksgGZqHWhBLFa8ek/nSEhlssM7H5UW0393Q5iI+Og55BYnHU
zFj/u68q4LJ3X542fbNYk1zJNTyEkpEdMNaiWuK2ibMc3t58TInRTGBmy0nWDXqI
uA649bqi2GNCupBl/0XuNc+52fP6EsmuMtCGhHQB0VHOCYCerb2wYyvAriPJAZx1
sqJXA3pArTQEkOWp1HU94232k5aoFAdLkEAVVL7eGDACwkAsC7F4hYfW7bHFl5J5
W2uXdHs8wu9XdPRVs4AURg0u5kfd1mmuGz3EQwIDAQABAoIBAELprkf8mfM7v9p/
HXBaGG1BDQcc3IFwKl1c1GLQAYMvdwHmaaLoWMXI1uKi7cwVPus4kAfOMaCOECnE
YO3nImJwn8iXx8rDBcEY5TTPQKv83JYLYKJ3BKD0fNFZ//yb2MWIqdKpRKLCluqQ
d8o1YWrbQz+irjf3aODXFfAZ4oh9EtKtcYYG1gCnYiUoLT7AyfCmSgH2BqTjTYpQ
BQn5DPoP+XzgGA5fp6VYT7MC49UbVg5DGTEWqGM8C5JcJSzXheesNXz1/1kbKppD
VhR4vCHl3cLjefpH1qj2FXEgIR2Q4mfo9GPOmboNacfn24vWqmtLBooKXxiOBnvR
ITUGuqECgYEA/GvarzEXGjDLmBtqQNx8sanvHwMfdmLjAS/YXMtDYSXxOJvtbg1A
ODteIXct1Lpo0RCzwyIpgO0mWYpw1jfR5a9k0agly5wj1TWwfN5m2DoL07Yf/ptJ
T+MH/FOkWQQnDk7uUirGa6mn5UqFFKjUpJIfvtAXIkwb9Sn8gC9cxpUCgYEA534f
gCZvpsVlHhQxohoDLBRPXYF3K9B6UGIzyrBU0mSTDGiXoxxJ6fpXmkilZ9/p798X
ZUbd35pK9uFrnYTGH/ApkJ6xfAxBXXuaA5/LsUNk2p29+deF55uRJfdKfa0t+Wsi
hgAn159zZ2O2fL0QT7minVkqv3An+jztkMOjYXcCgYA8Y5VC+oybYHck4dIuyFEw
tAYYNkgNq6TBgO/Ta3AASUqHj45jGNp3WYSwnvGhBiPTeJp5IbAqPxJAs8VBCsO1
npuRKRg5uRY9iQW7Kf4S1vhDYzwOTppgeWFMtboI7dxmKO4NTFyCH2miioyCH84X
vTnJDcGrG+hIZA4boLkibQKBgQCxWM6JPukKuu9cu9vKdGM5wdWLrUO/LViHfLRw
8+YHyUVenEKPk3yM5F14tW8CxWhTfHZQAJq6U1pbCReI9c9TaIpvYSRYBIuPMsIr
RKCojRkW4wJIBfVwGp54l5tZ7PLrwkgQoM3lQyMGcj28EpwMeAs8wLMfNq1p6IOY
BEmGwQKBgDVMXCvg9Rrb33vgqLpJyHwfVNyCbF2tpgzQUUMvG2xhjml3XfFXSTm5
bPpc6250qE+xqUKEatcuBZ1vdAwp0VgrifTNVod9oF+flztz+7dXOFaJEcBbOvh3
tQFd+SvcA0p9d7dMSbPYNM8dA1ofiZ99zfpfBdynpjG4myrTOb7M
-----END RSA PRIVATE KEY-----"/>
                        <div class="explanation"><fmt:message key="CameraKey"/></div>
                    </div>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/colorpicker.jsp"/>