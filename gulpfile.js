const gulp = require("gulp");
const sass = require("gulp-sass");
const concat = require("gulp-concat");
const nodeSass = require("node-sass");

sass.compiler = nodeSass;

gulp.task("sass", function () {
  return gulp
    .src("./src/styles/main.scss")
    .pipe(concat("style.scss"))
    .pipe(sass().on("error", sass.logError))
    .pipe(gulp.dest("./public/"));
});

gulp.task("sass:watch", function () {
  gulp.watch(
    "./src/styles/**/*.scss",
    { ignoreInitial: false },
    gulp.series("sass")
  );
});
