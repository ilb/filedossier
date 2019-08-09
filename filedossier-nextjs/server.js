/* eslint-disable no-console */
const express = require('express')
const next = require('next')
const os = require('os');
const config = require('./conf/config')

function onProxyReq(proxyReq, req, res) {
    proxyReq.setHeader('x-remote-user', os.userInfo().username);
}
const devProxy = {
    '/workflow-web': {
        target: {
            host: 'devel.net.ilb.ru',
            port: 443,
            protocol: 'https:',
            key: config.cert,
            cert: config.cert,
            passphrase: config.passphrase

        },
        logLevel: 'warn',
        changeOrigin: true,
        onProxyReq: onProxyReq

    }
}

const port = parseInt(process.env.PORT, 10) || 3000
const env = process.env.NODE_ENV
const dev = env !== 'production'
const app = next({
    dir: '.', // base directory where everything is, could move to src later
    dev
})

const handle = app.getRequestHandler()

let server
app.prepare()
        .then(() => {
            server = express()

            // Set up the proxy.
            if (dev && devProxy) {
                const proxyMiddleware = require('http-proxy-middleware')
                Object.keys(devProxy).forEach(function (context) {
                    server.use(proxyMiddleware(context, devProxy[context]))
                })
            }

            // Default catch-all handler to allow Next.js to handle all other routes
            server.all('*', (req, res) => handle(req, res))

            server.listen(port, err => {
                if (err) {
                    throw err
                }
                console.log(`> Ready on port ${port} [${env}]`)
            })
        })
        .catch(err => {
            console.log('An error occurred, unable to start the server')
            console.log(err)
        })
