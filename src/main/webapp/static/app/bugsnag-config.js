var script          = document.querySelector('script[data-release-stage]'),
    releaseStage    = script.getAttribute('data-release-stage'),
    userName        = script.getAttribute('data-user-name'),
    userMail        = script.getAttribute('data-user-mail');
    
Bugsnag.releaseStage = releaseStage;
Bugsnag.notifyReleaseStages = ["production"];
if (userName && userName !== " " && userMail){
    Bugsnag.user = {
        name: userName,
        email: userMail
    };
}