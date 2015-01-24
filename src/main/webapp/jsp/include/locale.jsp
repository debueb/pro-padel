<%@include file="/jsp/include/include.jsp"%>
<li class="navbar-icon navbar-locale-icon dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="flag ${pageContext.response.locale}"></span></a>
    <ul class="dropdown-menu" role="menu">
      <li><a href="?lang=en_US" class="flag en_US">Englisch</a></li>
      <li><a href="?lang=de_DE" class="flag de_DE">Deutsch</a></li>
      <li><a href="?lang=es_ES" class="flag es_ES">Espanol</a></li>
    </ul>
</li>