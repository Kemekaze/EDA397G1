var gulp = require('gulp')
var git = require('gulp-git')
var nodemon = require('gulp-nodemon')
var install = require('gulp-install')
const branch = 'develop'
const TIMEOUT = 1000

gulp.task('default', function (done) {
  git.init(function (err) {
    if (err) throw err
    git.checkout('origin/' + branch, function (err) {
      if (err) throw err
      git.pull('origin/' + branch, function (err) {
        if (err) throw err
        var installPipe = install()
        gulp.src(['./package.json'])
         .pipe(installPipe);
        installPipe.on('finish',function (){
          setTimeout(function () {
            var stream = nodemon({
              script: 'server.js',
              ext: 'html js'
            })
            stream.on('restart', function () {
              console.log('restarted!')
            })
            stream.on('crash', function () {
              console.error('Application has crashed!\n')
               stream.emit('restart', 10)  // restart the server in 10 seconds
            })
          }, TIMEOUT)
        })
      })
    })
  })
})
