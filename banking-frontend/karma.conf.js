module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],

    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage'),
      require('karma-sonarqube-reporter'), // ✅ ADD THIS
      require('@angular-devkit/build-angular/plugins/karma')
    ],

    client: {
      jasmine: {
        random: false
      },
      clearContext: false
    },

    coverageReporter: {
      dir: require('path').join(__dirname, './coverage'),
      subdir: '.',
      reporters: [
        { type: 'html' },
        { type: 'lcovonly' }, // ✅ REQUIRED by Sonar
        { type: 'text-summary' }
      ]
    },

    sonarqubeReporter: {
      basePath: 'src',
      filePattern: '**/*spec.ts',
      outputFolder: 'coverage/sonarqube',
      legacyMode: false
    },

    reporters: ['progress', 'kjhtml', 'sonarqube'], // ✅ ADD sonar

    browsers: ['Chrome'],

    autoWatch: false,
    singleRun: true,

    restartOnFileChange: true
  });
};
