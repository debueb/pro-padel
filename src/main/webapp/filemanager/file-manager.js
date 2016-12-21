"use strict";define("file-manager/app",["exports","ember","file-manager/resolver","ember-load-initializers","file-manager/config/environment"],function(e,t,n,a,l){var r=void 0;t.default.MODEL_FACTORY_INJECTIONS=!0,r=t.default.Application.extend({rootElement:"#file-manager",modulePrefix:l.default.modulePrefix,podModulePrefix:l.default.podModulePrefix,ready:function(){this.set("basePath",t.default.$(this.get("rootElement")).data("basePath")||"/admin/files"),this.set("onFileSelect",t.default.$(this.get("rootElement")).data("onFileSelect")||function(){console.log("Please specify an onFileSelect(url) callback function")})},Resolver:n.default}),(0,a.default)(r,l.default.modulePrefix),e.default=r}),define("file-manager/components/app-version",["exports","ember-cli-app-version/components/app-version","file-manager/config/environment"],function(e,t,n){var a=n.default.APP.name,l=n.default.APP.version;e.default=t.default.extend({version:l,name:a})}),define("file-manager/components/fa-icon",["exports","ember-font-awesome/components/fa-icon"],function(e,t){Object.defineProperty(e,"default",{enumerable:!0,get:function(){return t.default}})}),define("file-manager/components/fa-list",["exports","ember-font-awesome/components/fa-list"],function(e,t){Object.defineProperty(e,"default",{enumerable:!0,get:function(){return t.default}})}),define("file-manager/components/fa-stack",["exports","ember-font-awesome/components/fa-stack"],function(e,t){Object.defineProperty(e,"default",{enumerable:!0,get:function(){return t.default}})}),define("file-manager/components/file-folder",["exports","ember"],function(e,t){e.default=t.default.Component.extend({classNames:["file-manager-body-item"],classNameBindings:["isFolder:folder:file"],isFolder:function(){return"folder"===this.get("model").type}.property("model"),actions:{selectFolder:function(e){this.sendAction("selectFolder",e)},selectFile:function(e){this.sendAction("selectFile",e)}}})}),define("file-manager/components/file-input",["exports","ember","file-manager/templates/components/file-input"],function(e,t,n){e.default=t.default.Component.extend({classNameBindings:[":x-file-input","disabled:x-file-input--disabled"],attributeBindings:["accept"],tagName:"span",layout:n.default,tabindex:0,alt:"Upload",change:function(e){this.sendAction("action",e.target.files,this.resetInput.bind(this))},resetInput:function(){this.$(".x-file--input").val("")},randomId:t.default.computed(function(){return Math.random().toString(36).substring(7)})})}),define("file-manager/components/loading-spinner",["exports","ember"],function(e,t){e.default=t.default.Component.extend({})}),define("file-manager/helpers/number-to-human-size",["exports","ember-number-to-human-size/helpers/number-to-human-size"],function(e,t){Object.defineProperty(e,"default",{enumerable:!0,get:function(){return t.default}}),Object.defineProperty(e,"numberToHumanSize",{enumerable:!0,get:function(){return t.numberToHumanSize}})}),define("file-manager/helpers/pluralize",["exports","ember-inflector/lib/helpers/pluralize"],function(e,t){e.default=t.default}),define("file-manager/helpers/singularize",["exports","ember-inflector/lib/helpers/singularize"],function(e,t){e.default=t.default}),define("file-manager/initializers/app-version",["exports","ember-cli-app-version/initializer-factory","file-manager/config/environment"],function(e,t,n){e.default={name:"App Version",initialize:(0,t.default)(n.default.APP.name,n.default.APP.version)}}),define("file-manager/initializers/container-debug-adapter",["exports","ember-resolver/container-debug-adapter"],function(e,t){e.default={name:"container-debug-adapter",initialize:function(){var e=arguments[1]||arguments[0];e.register("container-debug-adapter:main",t.default),e.inject("container-debug-adapter:main","namespace","application:main")}}}),define("file-manager/initializers/data-adapter",["exports","ember"],function(e,t){e.default={name:"data-adapter",before:"store",initialize:t.default.K}}),define("file-manager/initializers/ember-data",["exports","ember-data/setup-container","ember-data/-private/core"],function(e,t,n){e.default={name:"ember-data",initialize:t.default}}),define("file-manager/initializers/export-application-global",["exports","ember","file-manager/config/environment"],function(e,t,n){function a(){var e=arguments[1]||arguments[0];if(n.default.exportApplicationGlobal!==!1){var a;if("undefined"!=typeof window)a=window;else if("undefined"!=typeof global)a=global;else{if("undefined"==typeof self)return;a=self}var l,r=n.default.exportApplicationGlobal;l="string"==typeof r?r:t.default.String.classify(n.default.modulePrefix),a[l]||(a[l]=e,e.reopen({willDestroy:function(){this._super.apply(this,arguments),delete a[l]}}))}}e.initialize=a,e.default={name:"export-application-global",initialize:a}}),define("file-manager/initializers/injectStore",["exports","ember"],function(e,t){e.default={name:"injectStore",before:"store",initialize:t.default.K}}),define("file-manager/initializers/store",["exports","ember"],function(e,t){e.default={name:"store",after:"ember-data",initialize:t.default.K}}),define("file-manager/initializers/transforms",["exports","ember"],function(e,t){e.default={name:"transforms",before:"store",initialize:t.default.K}}),define("file-manager/instance-initializers/ember-data",["exports","ember-data/-private/instance-initializers/initialize-store-service"],function(e,t){e.default={name:"ember-data",initialize:t.default}}),define("file-manager/resolver",["exports","ember-resolver"],function(e,t){e.default=t.default}),define("file-manager/router",["exports","ember","file-manager/config/environment"],function(e,t,n){var a=t.default.Router.extend({location:n.default.locationType,rootURL:n.default.rootURL});a.map(function(){}),e.default=a}),define("file-manager/routes/index",["exports","ember"],function(e,t){e.default=t.default.Route.extend({ajax:t.default.inject.service(),modelObject:t.default.Object.create(),beforeModel:function(){this.set("basePath",t.default.getOwner(this).get("application").get("basePath")),this.set("onFileSelect",t.default.getOwner(this).get("application").get("onFileSelect")),this.set("app",t.default.getOwner(this).get("application"))},model:function(){var e=this;return this.get("modelObject").showSpinner=!0,this.get("ajax").request(this.get("basePath")+"/list",{contentType:"application/json"}).then(function(t){return e.get("modelObject").set("paths",[]),e.get("modelObject").data=[],e.get("modelObject").data.pushObjects(t),e.get("modelObject").set("showSpinner",!1),e.get("modelObject")})},updateModel:function(){var e=this;this.get("modelObject").set("showSpinner",!0);var t=this.get("modelObject").get("paths").map(function(e){return e.name}).join("/");this.get("ajax").request(this.get("basePath")+"/list",{data:{path:t},contentType:"application/json"}).then(function(t){e.get("modelObject").data.clear().pushObjects(t),e.get("modelObject").set("showSpinner",!1)})}.observes("modelObject.paths.[]"),actions:{selectFolder:function(e){this.get("modelObject").get("paths").pushObject(e),this.get("modelObject").data.clear()},selectFile:function(e){this.send("triggerOnSelectFile",e.url)},selectLevel:function(e){this.get("modelObject").set("paths",this.get("modelObject").get("paths").slice(0,e+1)),this.get("modelObject").data.clear()},didSelectFiles:function(e){var t=this;this.get("modelObject").set("showSpinner",!0);var n=e[0];if(!/^image\//.test(n.type))return void alert("Currently only image uploads are supported.");var a=new FormData;return a.append("file",n),this.get("ajax").post(this.get("basePath")+"/upload",{dataType:"text",data:a,enctype:"multipart/form-data",cache:!1,contentType:!1,processData:!1}).then(function(e){t.send("triggerOnSelectFile",e)}).catch(function(e){alert(e),this.get("modelObject").set("showSpinner",!1)})},triggerOnSelectFile:function(e){this.get("modelObject").set("showSpinner",!1),this.get("onFileSelect")(e),this.send("close")},close:function(){this.get("app").destroy()}}})}),define("file-manager/services/ajax",["exports","ember-ajax/services/ajax"],function(e,t){Object.defineProperty(e,"default",{enumerable:!0,get:function(){return t.default}})}),define("file-manager/templates/components/file-folder",["exports"],function(e){e.default=Ember.HTMLBars.template(function(){var e=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:1,column:0},end:{line:6,column:0}},moduleName:"file-manager/templates/components/file-folder.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createElement("a"),a=e.createTextNode("\n  ");e.appendChild(n,a);var a=e.createElement("i");e.setAttribute(a,"class","fa fa-folder"),e.appendChild(n,a);var a=e.createTextNode("\n  ");e.appendChild(n,a);var a=e.createComment("");e.appendChild(n,a);var a=e.createTextNode("\n");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=e.childAt(t,[0]),l=new Array(2);return l[0]=e.createElementMorph(a),l[1]=e.createMorphAt(a,3,3),l},statements:[["element","action",["selectFolder",["get","model",["loc",[null,[2,27],[2,32]]],0,0,0,0]],[],["loc",[null,[2,3],[2,34]]],0,0],["content","model.name",["loc",[null,[4,2],[4,16]]],0,0,0,0]],locals:[],templates:[]}}(),t=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:6,column:0},end:{line:11,column:0}},moduleName:"file-manager/templates/components/file-folder.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createElement("a"),a=e.createTextNode("\n  ");e.appendChild(n,a);var a=e.createElement("img");e.appendChild(n,a);var a=e.createTextNode("\n  ");e.appendChild(n,a);var a=e.createComment("");e.appendChild(n,a);var a=e.createTextNode("\n");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=e.childAt(t,[0]),l=e.childAt(a,[1]),r=new Array(3);return r[0]=e.createElementMorph(a),r[1]=e.createAttrMorph(l,"src"),r[2]=e.createMorphAt(a,3,3),r},statements:[["element","action",["selectFile",["get","model",["loc",[null,[7,25],[7,30]]],0,0,0,0]],[],["loc",[null,[7,3],[7,32]]],0,0],["attribute","src",["concat",[["get","model.url",["loc",[null,[8,14],[8,23]]],0,0,0,0]],0,0,0,0,0],0,0,0,0],["inline","number-to-human-size",[["get","model.fileSize",["loc",[null,[9,25],[9,39]]],0,0,0,0]],[],["loc",[null,[9,2],[9,41]]],0,0]],locals:[],templates:[]}}();return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:1,column:0},end:{line:12,column:0}},moduleName:"file-manager/templates/components/file-folder.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createComment("");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=new Array(1);return a[0]=e.createMorphAt(t,0,0,n),e.insertBoundary(t,0),e.insertBoundary(t,null),a},statements:[["block","if",[["get","isFolder",["loc",[null,[1,6],[1,14]]],0,0,0,0]],[],0,1,["loc",[null,[1,0],[11,7]]]]],locals:[],templates:[e,t]}}())}),define("file-manager/templates/components/file-input",["exports"],function(e){e.default=Ember.HTMLBars.template(function(){var e=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:5,column:2},end:{line:7,column:2}},moduleName:"file-manager/templates/components/file-input.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createTextNode("    ");e.appendChild(t,n);var n=e.createComment("");e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=new Array(1);return a[0]=e.createMorphAt(t,1,1,n),a},statements:[["content","yield",["loc",[null,[6,4],[6,13]]],0,0,0,0]],locals:[],templates:[]}}(),t=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:7,column:2},end:{line:9,column:2}},moduleName:"file-manager/templates/components/file-input.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createTextNode("    ");e.appendChild(t,n);var n=e.createComment("");e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=new Array(1);return a[0]=e.createMorphAt(t,1,1,n),a},statements:[["content","alt",["loc",[null,[8,4],[8,11]]],0,0,0,0]],locals:[],templates:[]}}();return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:1,column:0},end:{line:11,column:0}},moduleName:"file-manager/templates/components/file-input.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createComment("");e.appendChild(t,n);var n=e.createTextNode("\n\n");e.appendChild(t,n);var n=e.createElement("label"),a=e.createTextNode("\n");e.appendChild(n,a);var a=e.createComment("");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=e.childAt(t,[2]),l=new Array(3);return l[0]=e.createMorphAt(t,0,0,n),l[1]=e.createAttrMorph(a,"for"),l[2]=e.createMorphAt(a,1,1),e.insertBoundary(t,0),l},statements:[["inline","input",[],["id",["subexpr","@mut",[["get","randomId",["loc",[null,[1,11],[1,19]]],0,0,0,0]],[],[],0,0],"type","file","class","x-file--input","name",["subexpr","@mut",[["get","name",["loc",[null,[1,59],[1,63]]],0,0,0,0]],[],[],0,0],"disabled",["subexpr","@mut",[["get","disabled",["loc",[null,[2,11],[2,19]]],0,0,0,0]],[],[],0,0],"multiple",["subexpr","@mut",[["get","multiple",["loc",[null,[2,29],[2,37]]],0,0,0,0]],[],[],0,0],"tabindex",["subexpr","@mut",[["get","tabindex",["loc",[null,[2,47],[2,55]]],0,0,0,0]],[],[],0,0],"accept",["subexpr","@mut",[["get","accept",["loc",[null,[2,63],[2,69]]],0,0,0,0]],[],[],0,0]],["loc",[null,[1,0],[2,71]]],0,0],["attribute","for",["concat",[["get","randomId",["loc",[null,[4,14],[4,22]]],0,0,0,0]],0,0,0,0,0],0,0,0,0],["block","if",[["get","hasBlock",["loc",[null,[5,8],[5,16]]],0,0,0,0]],[],0,1,["loc",[null,[5,2],[9,9]]]]],locals:[],templates:[e,t]}}())}),define("file-manager/templates/components/loading-spinner",["exports"],function(e){e.default=Ember.HTMLBars.template(function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:1,column:0},end:{line:4,column:0}},moduleName:"file-manager/templates/components/loading-spinner.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createElement("div");e.setAttribute(n,"class","file-manager-spinner");var a=e.createTextNode("\n  ");e.appendChild(n,a);var a=e.createElement("i");e.setAttribute(a,"class","fa fa-spinner fa-spin fa-3x fa-fw"),e.appendChild(n,a);var a=e.createTextNode("\n");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(){return[]},statements:[],locals:[],templates:[]}}())}),define("file-manager/templates/index",["exports"],function(e){e.default=Ember.HTMLBars.template(function(){var e=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:1,column:0},end:{line:3,column:0}},moduleName:"file-manager/templates/index.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createTextNode("  ");e.appendChild(t,n);var n=e.createComment("");e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=new Array(1);return a[0]=e.createMorphAt(t,1,1,n),a},statements:[["content","loading-spinner",["loc",[null,[2,2],[2,21]]],0,0,0,0]],locals:[],templates:[]}}(),t=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:12,column:8},end:{line:14,column:8}},moduleName:"file-manager/templates/index.hbs"},isEmpty:!1,arity:2,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createTextNode("          ");e.appendChild(t,n);var n=e.createElement("a"),a=e.createTextNode("/ ");e.appendChild(n,a);var a=e.createComment("");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=e.childAt(t,[1]),l=new Array(2);return l[0]=e.createElementMorph(a),l[1]=e.createMorphAt(a,1,1),l},statements:[["element","action",["selectLevel",["get","index",["loc",[null,[13,36],[13,41]]],0,0,0,0]],[],["loc",[null,[13,13],[13,43]]],0,0],["content","path.name",["loc",[null,[13,46],[13,59]]],0,0,0,0]],locals:["path","index"],templates:[]}}(),n=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:16,column:12},end:{line:21,column:12}},moduleName:"file-manager/templates/index.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createTextNode("                ");e.appendChild(t,n);var n=e.createElement("a");e.setAttribute(n,"class","fa-stack");var a=e.createTextNode("\n                  ");e.appendChild(n,a);var a=e.createElement("i");e.setAttribute(a,"class","fa fa-file fa-stack-2x"),e.appendChild(n,a);var a=e.createTextNode("\n                  ");e.appendChild(n,a);var a=e.createElement("i");e.setAttribute(a,"class","fa fa-plus fa-inverse fa-stack-1x"),e.appendChild(n,a);var a=e.createTextNode("\n                ");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(){return[]},statements:[],locals:[],templates:[]}}(),a=function(){return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:26,column:10},end:{line:28,column:10}},moduleName:"file-manager/templates/index.hbs"},isEmpty:!1,arity:1,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createTextNode("              ");e.appendChild(t,n);var n=e.createComment("");e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=new Array(1);return a[0]=e.createMorphAt(t,1,1,n),a},statements:[["inline","file-folder",[],["model",["subexpr","@mut",[["get","obj",["loc",[null,[27,34],[27,37]]],0,0,0,0]],[],[],0,0],"selectFolder","selectFolder","selectFile","selectFile"],["loc",[null,[27,14],[27,91]]],0,0]],locals:["obj"],templates:[]}}();return{meta:{revision:"Ember@2.7.3",loc:{source:null,start:{line:1,column:0},end:{line:32,column:0}},moduleName:"file-manager/templates/index.hbs"},isEmpty:!1,arity:0,cachedFragment:null,hasRendered:!1,buildFragment:function(e){var t=e.createDocumentFragment(),n=e.createComment("");e.appendChild(t,n);var n=e.createElement("div");e.setAttribute(n,"class","file-manager");var a=e.createTextNode("\n    ");e.appendChild(n,a);var a=e.createElement("div");e.setAttribute(a,"class","file-manager-header");var l=e.createTextNode("Image Manager\n        ");e.appendChild(a,l);var l=e.createElement("div");e.setAttribute(l,"class","pull-right");var r=e.createTextNode("\n          ");e.appendChild(l,r);var r=e.createElement("span"),i=e.createElement("i");e.setAttribute(i,"class","fa fa-close"),e.appendChild(r,i),e.appendChild(l,r);var r=e.createTextNode("\n        ");e.appendChild(l,r),e.appendChild(a,l);var l=e.createTextNode("\n    ");e.appendChild(a,l),e.appendChild(n,a);var a=e.createTextNode("\n    ");e.appendChild(n,a);var a=e.createElement("div");e.setAttribute(a,"class","file-manager-toolbar");var l=e.createTextNode("\n        ");e.appendChild(a,l);var l=e.createElement("a"),r=e.createElement("i");e.setAttribute(r,"class","fa fa-2x fa-home"),e.appendChild(l,r),e.appendChild(a,l);var l=e.createTextNode("\n");e.appendChild(a,l);var l=e.createComment("");e.appendChild(a,l);var l=e.createTextNode("        ");e.appendChild(a,l);var l=e.createElement("div");e.setAttribute(l,"class","pull-right");var r=e.createTextNode("\n");e.appendChild(l,r);var r=e.createComment("");e.appendChild(l,r);var r=e.createTextNode("        ");e.appendChild(l,r),e.appendChild(a,l);var l=e.createTextNode("\n    ");e.appendChild(a,l),e.appendChild(n,a);var a=e.createTextNode("\n    ");e.appendChild(n,a);var a=e.createElement("div"),l=e.createTextNode("\n      ");e.appendChild(a,l);var l=e.createElement("div");e.setAttribute(l,"class","file-manager-body");var r=e.createTextNode("\n");e.appendChild(l,r);var r=e.createComment("");e.appendChild(l,r);var r=e.createTextNode("      ");e.appendChild(l,r),e.appendChild(a,l);var l=e.createTextNode("\n    ");e.appendChild(a,l),e.appendChild(n,a);var a=e.createTextNode("\n");e.appendChild(n,a),e.appendChild(t,n);var n=e.createTextNode("\n");return e.appendChild(t,n),t},buildRenderNodes:function(e,t,n){var a=e.childAt(t,[1]),l=e.childAt(a,[1,1,1]),r=e.childAt(a,[3]),i=e.childAt(r,[1]),o=new Array(6);return o[0]=e.createMorphAt(t,0,0,n),o[1]=e.createElementMorph(l),o[2]=e.createElementMorph(i),o[3]=e.createMorphAt(r,3,3),o[4]=e.createMorphAt(e.childAt(r,[5]),1,1),o[5]=e.createMorphAt(e.childAt(a,[5,1]),1,1),e.insertBoundary(t,0),o},statements:[["block","if",[["get","model.showSpinner",["loc",[null,[1,6],[1,23]]],0,0,0,0]],[],0,null,["loc",[null,[1,0],[3,7]]]],["element","action",["close"],[],["loc",[null,[7,16],[7,34]]],0,0],["element","action",["selectLevel",-1],[],["loc",[null,[11,11],[11,38]]],0,0],["block","each",[["get","model.paths",["loc",[null,[12,16],[12,27]]],0,0,0,0]],[],1,null,["loc",[null,[12,8],[14,17]]]],["block","file-input",[],["multiple",!0,"action","didSelectFiles","accept","image/png,image/jpg"],2,null,["loc",[null,[16,12],[21,27]]]],["block","each",[["get","model.data",["loc",[null,[26,18],[26,28]]],0,0,0,0]],[],3,null,["loc",[null,[26,10],[28,19]]]]],locals:[],templates:[e,t,n,a]}}())}),define("file-manager/config/environment",["ember"],function(e){var t="file-manager";try{var n=t+"/config/environment",a=e.default.$('meta[name="'+n+'"]').attr("content"),l=JSON.parse(unescape(a));return{default:l}}catch(e){throw new Error('Could not read config from meta tag with name "'+n+'".')}}),runningTests||require("file-manager/app").default.create({name:"file-manager",version:"0.0.0+"});