import React from 'react';
import App, { Container } from 'next/app';
import { ApiClient, ProcessDefinitionsApi, ProcessInstancesApi } from '@ilb/workflow-api/dist';
import config from '../conf/config';
import Header from './components/header.js';


class MyApp extends App {
  // Only uncomment this method if you have blocking data requirements for
  // every single page in your application. This disables the ability to
  // perform automatic static optimization, causing every page in your app to
  // be server-side rendered.
  //
  setStateAsync(state) {
    return new Promise((resolve) => {
      this.setState(state, resolve)
    });
  }

  static async getInitialProps({ Component, router, ctx }) {
    let pageProps = {}
    let data = {}

    const api = new ProcessDefinitionsApi(config.workflowApiClient(null));
    // const data = await api.getProcessDefinitions({enabled: true});

    if (Component.getInitialProps) {
      pageProps = await Component.getInitialProps(ctx);
    }

    pageProps.data = await api.getProcessDefinitions({enabled: true});

    return { pageProps: { pageProps }, data }
  }

  state = {
    name: "to index.js",
    data: {},
  }


  render() {
    const { Component, pageProps } = this.props;

    return (
      <Container>
        <Header {...pageProps} {...this.state}/>
        <Component {...pageProps} {...this.state}/>
      </Container>
    );
  }
}

export default MyApp;
