# Seven GUIs

This application implements the first five of the [Seven Guis][seven-guis]
using ClojureScript along with [reagent][reagent] and [re-frame][re-frame]. The
styling utilizes [SASS][sass] and [BEM][bem] to organize component styles into
distinct areas.

[seven-guis]: https://eugenkiss.github.io/7guis/
[reagent]: https://github.com/reagent-project/reagent
[re-frame]: https://github.com/day8/re-frame
[sass]: https://sass-lang.com/
[bem]: http://getbem.com/

The `main` branch build is hosted at: https://pax-seven-guis.netlify.app

## Building

To build the application for release, run:

```sh
npm run build:release
```

Once the build completes, you can view the static output by running:

```sh
cd public && npx http-server
```

and visiting `http://localhost:8001` in your web browser.

## Development

To start the application in development mode, run:

```sh
npm run watch:cljs
# In another terminal window
npm run watch:sass
```

and visit `http://localhost:8001` in your web browser. Changing ClojureScript or
SASS files will rebuild the changed files, and the result will appear in your
browser.
