const path = require('path');

module.exports = {
    globalSetup: './config/setup.js',
    globalTeardown: './config/teardown.js',
    testEnvironment: './config/puppeteer_environment.js',
    rootDir: path.join(__dirname, '..'),
    testMatch: ['**/tests/**/*.js'],
};
