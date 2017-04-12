var packageJSON = require('./package.json'),
    path = require('path'),
    webpack = require('webpack'),
    LiveReloadPlugin = require('webpack-livereload-plugin');

const PATHS = {
    /* Openshift Online 2.x has a non upgradable Maven 3.0.4 installation and is not 
     * compatible with frontend-maven-plugin, which requires Maven >3.1.0
     * Therefore, we depend on the development environment to generate our bundle into our sources
    build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'app')
    */
    build: path.join(__dirname, 'src', 'main', 'webapp', 'app', 'dist')
};

module.exports = {
    entry: {
        main: './src/main/webapp/app/main.js'
    },

    output: {
        path: PATHS.build,
        filename: '[name].js'
    },

    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['env']
                    }
                }
            }
        ]
    },

    devtool: "source-map",

    plugins: [
        new LiveReloadPlugin()
    ]
};
