var packageJSON = require('./package.json');
var path = require('path');
var webpack = require('webpack');

const PATHS = {
    build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'app')
};

module.exports = {
    entry: './src/main/webapp/app/index.js',

    output: {
        path: PATHS.build,
        filename: 'bundle.js'
    }
};
