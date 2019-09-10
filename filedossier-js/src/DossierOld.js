import React, { Component } from "react";
import { ApiClient, DefaultApi } from "./openapi/src";
import { Table, Button } from "semantic-ui-react";

class Dossier extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dossier: {
                loading: false,
                error: null,
                value: null
            },
            delete: {
                loading: false,
                error: null,
                value: null
            }
        };
        this.client = new ApiClient();
        // this.client.basePath = 'https://devel.net.ilb.ru';
        this.client.basePath = "/filedossier-web/web";
        this.dossierApi = new DefaultApi(this.client);
    }
    componentDidMount() {
        const { dossierKey, dossierPackage, dossierCode } = this.props;
        this.getDossier(dossierKey, dossierPackage, dossierCode);
    }

    getDossier = (dossierKey, dossierPackage, dossierCode) => {
        this.setState({ dossier: { loading: true, error: null } });
        this.dossierApi.getDossier(
            dossierKey,
            dossierPackage,
            dossierCode,
            this.getDossierCallback
        );
    };

    removeDossier = event => {
        const code = event.target.dataset.code;
        alert(code);
    };

    //    formSubmit = (values) => {
    //        const {dossierKey, dossierPackage, dossierCode} = values;
    //
    //    }

    getDossierCallback = (error, data, response) => {
        if (error) {
            this.setState({ dossier: { loading: false, error, value: null } });
        } else {
            console.log("API called successfully.", { data, response });
            this.setState({
                dossier: { loading: false, value: response.body }
            });
        }
    };

    render() {
        console.log(this.state);
        const { loading, error, value } = this.state.dossier;
        if (loading) {
            return <div className="fileDosser">Загрузка</div>;
        } else if (error) {
            return <div className="fileDosser">Ошибка {error}</div>;
        }

        return (
            <div className="fileDosser">
                {value && (
                    <div>
                        Name: {value.name}
                        {value.dossierFile && (
                            <Table celled>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.HeaderCell>
                                            Файл
                                        </Table.HeaderCell>
                                        <Table.HeaderCell>
                                            Действие
                                        </Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>

                                <Table.Body>
                                    {value.dossierFile.map((file, index) => (
                                        <Table.Row key={index}>
                                            <Table.Cell>{file.name}</Table.Cell>
                                            <Table.Cell>
                                                <Button
                                                    data-code={file.code}
                                                    onClick={
                                                        this.removeDossierFile
                                                    }
                                                    content="Удалить"
                                                />
                                            </Table.Cell>
                                        </Table.Row>
                                    ))}
                                </Table.Body>
                            </Table>
                        )}
                    </div>
                )}
            </div>
        );
    }
}

export default Dossier;
