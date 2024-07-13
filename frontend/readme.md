Compilation workflow:
1. `mill frontend.fastLinkJs`
   - compiles scala into js (no optimization)
   - result is in `out/frontend/fastLinkJS.dest`
2. `npm run build` (`npx vite build ./frontend/resources --outDir ../../dest --emptyOutDir`)
   - vite takes `frontend/resources` as a web directory to serve `index.html`
   - there is also `index.js` that imports the compiled js from previous step
   - combines everything together and puts it into `dest` folder
3. `npx cap sync`
   - copies `dest/` into `adnroid/src/main/assets/public/assets`
4. `npx run run android`
   - compiles artifacts and runs from `android/` folder

Dev setup:
   - `mill --watch frontend.fastLinkJS`
   - `npm run dev`

Requirements:
    - `android/app/google-services.json` for push notifications support

List of things to try:
- ~~web components~~
- ~~facade for iconic component~~
  - ~~inner text~~
  - ~~listeners~~
  - ~~reactivness~~
- ~~routes~~
  - using direct url to access page not working (might be vite issue)
- ~~app container~~
- ~~notifications (through google fcm)~~
- barcode scanner
- rpc