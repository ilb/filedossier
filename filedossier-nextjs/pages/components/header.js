import Link from 'next/link';
import { Menu, Icon } from 'semantic-ui-react'
import config from '../../conf/config';
// import React, { useState, useEffect, useRef } from 'react';
import React, { useState, useEffect, useRef } from 'react';
import {  Message, Loader, Select, Button } from 'semantic-ui-react'
import { ApiClient, ProcessDefinitionsApi, ProcessInstancesApi } from '@ilb/workflow-api/dist';
// import { useResource } from '../api/ReactHelper';
import { Dropdown } from 'semantic-ui-react';
import '@bb/semantic-ui-css/semantic.min.css'
// import config from '../../conf/config';
import superagent from "superagent";


const ProcessSelectorContainer = (props) => {
    const errorHandler = (data) => {
        alert(data.error);
    }

    const startProcess = async (optionValue_) => {
        console.log('optionValue', optionValue || optionValue_);
        if (!(optionValue || optionValue_)) {
          alert('Процесс не выбран');
          return false;
        }

        const res = await superagent.post(process.env.API_PATH + "/createProcessInstanceAndNext") // /api/createProcessInstanceAndNext.js
          .query({processDefinitionId: optionValue || optionValue_})
          .send({})
          // .then(res => document.location=res.headers['x-location'].replace(/https:\/\/devel.net.ilb.ru\/workflow-js/,"http://" + document.location.host));
        if (res && res.headers) {
          console.log(res.headers['x-location']);
          document.location=res.headers['x-location'].replace(/https:\/\/devel.net.ilb.ru\/workflow-js/,"http://" + document.location.host + "/workflow");
        }
    };

    //-------------
    const data = props && props.props && props.props.pageProps && props.props.pageProps.data;
    const processDefinition = data && data.processDefinition;
    const options = [];
    const createOption = (optionData) => {
      return { key: optionData.name, text: optionData.definitionName, value: optionData.id }
    };
    if (processDefinition) {
      Object.keys(processDefinition).forEach(el => options.push(createOption(processDefinition[el])));
    }

    const [optionValue, setOptionValue] = useState(null);

    return <div className="processSelectorContainer">
      {data && data.loading && <Loader active /> }
      {data && data.error && <Message error visible content={data.error}/> }
      {processDefinition && <div>
        <Menu compact>
          <Dropdown
            // inline
            text='Запустить процесс'
            onChange={(e, data) => {
              console.log('12344', data.value);
              setOptionValue(data.value);
              startProcess(data.value);
            }}
            // onClick={startProcess}
            options={options}
            // placeholder='Выбрать процесс'
            // selection
            value={optionValue}
            item
            simple
          />
        </Menu>
      </div>}
    </div>;
}

const linkStyle = {
  marginRight: 15
};

const Header = (props) => {
  return <div>
    <Menu style={{ marginBottom: '1.5rem' }}>
        <Menu.Item
          name='Рабочий лист'
          href='/workflow/worklist'
        />
        <ProcessSelectorContainer
          props={props}
          />
      </Menu>
  </div>;
};

export default Header;
