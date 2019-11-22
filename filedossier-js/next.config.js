const withCSS = require('@zeit/next-css');
// see https://github.com/zeit/next.js/issues/257
const isProd = process.env.NODE_ENV === 'production';
const prefix = isProd ? '/filedossier' : '';

module.exports = withCSS({
  assetPrefix: prefix, // affects page bundles and app/commons/vendor scripts
  env: {
    API_PATH: prefix + '/api',
  },
  webpack: (config, { isServer }) => {
    if (!isServer) {
      config.node = {
        fs: 'empty',
      };
    }
    config.module.rules.unshift({
      test: /\.(js|jsx)$/,
      exclude: /node_modules/,
      loader: 'eslint-loader',
      enforce: 'pre',
      options: {
        quiet: true,
        failOnError: isProd,
        failOnWarning: isProd,
        emitError: false,
        emitWarning: true,
      },
    });

    config.module.rules.push({
      test: /\.(png|jpg|svg|eot|otf|ttf|woff|woff2)$/,
      use: {
        loader: 'url-loader',
        options: {
          limit: 8192,
          publicPath: prefix + '/_next/static/',
          outputPath: 'static/',
          name: '[name].[ext]',
        },
      },
    });

    if (isProd) {
      config.output.publicPath = prefix + config.output.publicPath; // affects 'chunks'
    }
    return config;
  },
});
