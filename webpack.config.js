var packageJSON = require('./package.json');
var path = require('path');
var webpack = require('webpack');

const PATHS = {
    /* Openshift Online 2.x has a non upgradable Maven 3.0.4 installation and is not 
     * compatible with frontend-maven-plugin, which requires Maven >3.1.0
     * Therefore, we depend on the development environment to generate our bundle into our sources
    build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'app')
    */
    build: path.join(__dirname, 'src', 'main', 'webapp', 'app')
};

module.exports = {
    entry: './src/main/webapp/app/index.js',

    output: {
        path: PATHS.build,
        filename: 'bundle.js'
    }
};
