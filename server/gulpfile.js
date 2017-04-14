var gulp = require('gulp')
var git = require('gulp-git')
var nodunemon = require('gulp-nodemon')
var r = require('gulp-run')
const branch = 'develop'
const TIMEOUT = 1000

gulp.task('default', function (done) {
  gulp.src('../').pipe(
    git.init(function (err) {
      if (err) throw err
      git.checkout(branch, function (err) {
        if (err) throw err
        git.pull('origin/' + branch, function (err) {
          if (err) throw err
          gulp.src('./server').pipe(
            run('npm install').exec(function(){
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
            })
          )          
        })
      })
    })
  )
})
