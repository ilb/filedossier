const withCSS = require('@zeit/next-css')
// see https://github.com/zeit/next.js/issues/257
const isProd = process.env.NODE_ENV === 'production'
const prefix = isProd ? '/workflow' : '';
module.exports = withCSS({
    assetPrefix: prefix, // affects page bundles and app/commons/vendor scripts
    env: {
        API_PATH: prefix + '/api'
    },
    webpack: (config) => {

        config.module.rules.push({
            test: /\.(png|svg|eot|otf|ttf|woff|woff2)$/,
            use: {
                loader: 'url-loader',
                options: {
                    limit: 8192,
                    publicPath: prefix + '/_next/static/',
                    outputPath: 'static/',
                    name: '[name].[ext]'
                }
            }
        })
        if (isProd) {
            config.output.publicPath = prefix + config.output.publicPath; // affects 'chunks'
        }
        return config;
    },
});
