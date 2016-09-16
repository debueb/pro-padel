<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Event}: <fmt:message key="AddGame"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="ajaxify" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <table class="table-full-width table-fixed">
                        <thead>
                            <th class="text-center"><fmt:message key="Team1"/></th>
                            <th class="text-center"><fmt:message key="Team2"/></th>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <spf:select path="team1" class="select-multiple form-control" data-container="body">
                                        <spf:options items="${Event.participants}" itemValue="UUID"/>
                                    </spf:select>
                                </td>
                                <td>
                                    <spf:select path="team2" class="select-multiple form-control" data-container="body">
                                        <spf:options items="${Event.participants}" itemValue="UUID"/>
                                    </spf:select>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
