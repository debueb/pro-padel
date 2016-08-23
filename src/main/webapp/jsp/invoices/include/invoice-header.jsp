<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="Invoice"/></title>
    
    <style type="text/css">
        body{
            font-family: Arial;
        }
        .letter{
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .logo{
            float: right;
        }
        .clearfix{
            clear:both;
        }
        .small{
            font-size: 10px;
        }
        .date{
            float:right;
        }
        .margin-bottom{
            margin-bottom: 20px;
        }
        .margin-bottom-2{
            margin-bottom: 40px;
        }
        table.invoice{
            width: 100%;
        }
        table.invoice th, table.invoice td{
            text-align: left;
        }
        .centered{
            text-align: center;
        }
        .right{
            text-align: right;
        }
        @media all {
	.page-break	{ display: none; }
        }
        @media print {
                .page-break	{ display: block; page-break-before: always; }
        }
    </style>
</head>