Stack:
- build tool: mill
- serde: circe
- backend:
    - zio-http for web server
    - sloth for rpc
- frontend:
    - rendering/state: laminar
    - packaging: vite
    - native app: capacitor/ionic
        