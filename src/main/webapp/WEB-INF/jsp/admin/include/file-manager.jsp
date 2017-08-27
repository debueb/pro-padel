<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="container unit-4">
    <div class="row">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>FILE MANAGER DEMO</h4>
            </div>
            <div class="panel-body">

                <form method="POST">

                    <div class="relative">
                        <textarea name="message" class="form-control form-bottom-element text-editor"></textarea>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </form>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/jsp/include/footer.jsp"%>
<jsp:include page="/WEB-INF/jsp/admin/include/text-editor.jsp"/>