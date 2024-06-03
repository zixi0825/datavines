/* eslint-disable react/no-danger */
import React, { useRef, useState } from 'react';
import { ModalProps, Table } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import {
    useModal, useImmutable, usePersistFn, useWatch,
} from 'src/common';
import { useIntl } from 'react-intl';
import { $http } from '@/http';
import { defaultRender } from '@/utils/helper';
import {TJobQualityReportTableItem} from "@/type/JobQualityReport";
import ColumnExecutionResult from "./columnExecutionResult";
import {TableColumnsType} from "antd/lib";

type InnerProps = {
    [key: string]: any
}
const Inner = (props: InnerProps) => {
    const intl = useIntl();
    const [loading, setLoading] = useState(false);
    const [tableData, setTableData] = useState<{ list: TJobQualityReportTableItem[], total: number}>({ list: [], total: 0 });
    const [pageParams, setPageParams] = useState({
        pageNumber: 1,
        pageSize: 10,
    });
    const columns: ColumnsType<TJobQualityReportTableItem> = [
        {
            title: intl.formatMessage({ id: 'jobs_task_schema_name' }),
            dataIndex: 'databaseName',
            key: 'databaseName',
            width: 200,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_table_name' }),
            dataIndex: 'tableName',
            key: 'tableName',
            width: 200,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_column_name' }),
            dataIndex: 'columnName',
            key: 'columnName',
            width: 200,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_score' }),
            dataIndex: 'score',
            key: 'score',
            width: 200,
            render: (text: string) => <div>{text}</div>,
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_report_date' }),
            dataIndex: 'reportDate',
            key: 'reportDate',
            width: 200,
            render: (text: string) => <div>{text || '--'}</div>,
        }
    ];
    const getData = async () => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/quality-report/page', {
                datasourceId: props.record.datasourceId,
                schemaName : props.record.databaseName,
                tableName : props.record.tableName,
                reportDate: props.record.reportDate,
                ...pageParams,
            })) || [];
            setTableData({
                list: res?.records || [],
                total: res.total || 0,
            });
        } catch (error) {
        } finally {
            setLoading(false);
        }
    };
    useWatch([pageParams], async () => {
        getData();
    }, { immediate: true });
    const onChange = ({ current, pageSize }: any) => {
        setPageParams({
            pageNumber: current,
            pageSize,
        });
    };

    const expandedRowRender2 = (props: InnerProps) => {
        const intl = useIntl();
        const [tableData, setTableData] = useState<{ list: TJobQualityReportTableItem[], total: number}>({ list: [], total: 0 });
        const columns: ColumnsType<TJobQualityReportTableItem> = [
            {
                title: intl.formatMessage({ id: 'jobs_task_schema_name' }),
                dataIndex: 'databaseName',
                key: 'databaseName',
                width: 200,
                render: (text: string) => defaultRender(text, 300),
            },
            {
                title: intl.formatMessage({ id: 'jobs_task_table_name' }),
                dataIndex: 'tableName',
                key: 'tableName',
                width: 200,
                render: (text: string) => defaultRender(text, 300),
            },
            {
                title: intl.formatMessage({ id: 'jobs_task_column_name' }),
                dataIndex: 'columnName',
                key: 'columnName',
                width: 200,
                render: (text: string) => defaultRender(text, 300),
            },
            {
                title: intl.formatMessage({ id: 'jobs_task_score' }),
                dataIndex: 'score',
                key: 'score',
                width: 200,
                render: (text: string) => <div>{text}</div>,
            },
            {
                title: intl.formatMessage({ id: 'jobs_task_report_date' }),
                dataIndex: 'reportDate',
                key: 'reportDate',
                width: 200,
                render: (text: string) => <div>{text || '--'}</div>,
            }
        ];
        const getData = async () => {
            try {
                setLoading(true);
                const res = (await $http.post('/job/quality-report/page', {
                    datasourceId: props.record.datasourceId,
                    schemaName : props.record.databaseName,
                    tableName : props.record.tableName,
                    ...pageParams,
                })) || [];
                setTableData({
                    list: res?.records || [],
                    total: res.total || 0,
                });
            } catch (error) {
            } finally {
                setLoading(false);
            }
        };
        useWatch([pageParams], async () => {
            getData();
        }, { immediate: true });

        // @ts-ignore
        return (
            <div>
                <Table<TJobQualityReportTableItem>
                    rowKey="id"
                    columns={columns}
                    dataSource={tableData.list || []}
                />
            </div>
        );
    };

    return (
        <div>
            <Table<TJobQualityReportTableItem>
                size="middle"
                loading={loading}
                rowKey="id"
                columns={columns}
                dataSource={tableData.list || []}
                onChange={onChange}
                scroll={{
                    x: (columns.length) * 120,
                }}

                expandable={{
                    // eslint-disable-next-line react/no-unstable-nested-components
                    expandedRowRender: (record: TJobQualityReportTableItem) => (
                        <ColumnExecutionResult record = {record}/>
                    )

                }}
                pagination={{
                    size: 'small',
                    total: tableData.total,
                    showSizeChanger: true,
                    current: pageParams.pageNumber,
                    pageSize: pageParams.pageSize,
                }}
            />
        </div>
    );
};

export const qualityReportDetailModal = (options: ModalProps) => {
    const intl = useIntl();
    const recordRef = useRef<any>();
    const onOk = usePersistFn(() => {
        hide();
    });
    const {
        Render, hide, show, ...rest
    } = useModal<any>({
        title: `${intl.formatMessage({ id: 'jobs_task_report_detail' })}`,
        className: 'dv-modal-fullscreen',
        footer: null,
        width: '90%',
        ...(options || {}),
        afterClose() {
            recordRef.current = null;
        },
        onOk,
    });
    return {
        Render: useImmutable(() => (<Render><Inner record={recordRef.current} /></Render>)),
        show(record: any) {
            recordRef.current = record;
            show(record);
        },
        ...rest,
    };
};
