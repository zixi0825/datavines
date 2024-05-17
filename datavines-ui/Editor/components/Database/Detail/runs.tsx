import React, {
    forwardRef, useEffect, useImperativeHandle, useState,
} from 'react';
import { Table } from 'antd';
import { useIntl } from 'react-intl';
import { ColumnsType } from 'antd/es/table';
import { $http } from '@/http';
import {IF, useWatch} from '@/common';
import {TJobsInstanceTableData, TJobsInstanceTableItem} from '@/type/JobsInstance';
import { useInstanceErrorDataModal } from '@/view/Main/HomeDetail/Jobs/useInstanceErrorDataModal';
import { useInstanceResult } from '@/view/Main/HomeDetail/Jobs/useInstanceResult';
import { useLogger } from '@/view/Main/HomeDetail/Jobs/useLogger';
import {defaultRender} from "utils/helper";

// eslint-disable-next-line react/no-unused-prop-types
const Index = (props: any, ref:any) => {
    const { id } = props;
    useImperativeHandle(ref, () => ({ getData }));
    const intl = useIntl();
    const [loading, setLoading] = useState(false);
    const [pageParams, setPageParams] = useState({
        pageNumber: 1,
        pageSize: 10,
    });
    const { Render: RenderErrorDataModal, show: showErrorDataModal } = useInstanceErrorDataModal({});
    const { Render: RenderResultModal, show: showResultModal } = useInstanceResult({});
    const { Render: RenderLoggerModal, show: showLoggerModal } = useLogger({});
    const onStop = async (record: TJobsInstanceTableItem) => {
        try {
            setLoading(true);
            await $http.delete(`/task/kill/${record.id}`);
            getData();
        } catch (error) {
        } finally {
            setLoading(false);
        }
    };
    const onLog = (record: TJobsInstanceTableItem) => {
        showLoggerModal(record);
    };
    const onResult = (record: TJobsInstanceTableItem) => {
        showResultModal(record);
    };
    const onErrorData = (record: TJobsInstanceTableItem) => {
        showErrorDataModal(record);
    };

    const onChange = ({ current, pageSize }: any) => {
        setPageParams({
            pageNumber: current,
            pageSize,
        });
        getData();
    };

    const [tableData, setTableData] = useState<TJobsInstanceTableData>({ list: [], total: 0 });
    const columns: ColumnsType<TJobsInstanceTableItem> = [
        {
            title: intl.formatMessage({ id: 'jobs_task_name' }),
            dataIndex: 'name',
            key: 'name',
            width: 250,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_schema_name' }),
            dataIndex: 'schemaName',
            key: 'schemaName',
            width: 100,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_table_name' }),
            dataIndex: 'tableName',
            key: 'tableName',
            width: 150,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_column_name' }),
            dataIndex: 'columnName',
            key: 'columnName',
            width: 150,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_metric_type' }),
            dataIndex: 'metricType',
            key: 'metricType',
            width: 180,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_status' }),
            dataIndex: 'status',
            key: 'status',
            width: 140,
            render: (text: string) => <div>{text}</div>,
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_check_status' }),
            dataIndex: 'checkState',
            key: 'checkState',
            width: 140,
            render: (text: string) => <div>{text}</div>,
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_start_time' }),
            dataIndex: 'startTime',
            key: 'startTime',
            width: 180,
            render: (text: string) => <div>{text || '--'}</div>,
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_end_time' }),
            dataIndex: 'endTime',
            key: 'endTime',
            width: 180,
            render: (text: string) => <div>{text || '--'}</div>,
        },
        {
            title: intl.formatMessage({ id: 'common_action' }),
            fixed: 'right',
            key: 'right',
            dataIndex: 'right',
            width: 200,
            render: (text: string, record: TJobsInstanceTableItem) => (
                <>
                    <IF visible={record.status === 'submitted' || record.status === 'running'|| record.status === '已提交' || record.status === '执行中'}>
                        <a style={{ marginRight: 5 }} onClick={() => { onStop(record); }}>{intl.formatMessage({ id: 'jobs_task_stop_btn' })}</a>
                    </IF>
                    <a style={{ marginRight: 5 }} onClick={() => { onLog(record); }}>{intl.formatMessage({ id: 'jobs_task_log_btn' })}</a>
                    <a style={{ marginRight: 5 }} onClick={() => { onResult(record); }}>{intl.formatMessage({ id: 'jobs_task_result' })}</a>
                    <a style={{ marginRight: 5 }} onClick={() => { onErrorData(record); }}>{intl.formatMessage({ id: 'jobs_task_error_data' })}</a>
                </>
            ),
        },
    ];

    const getData = async (values: any =null) => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/execution/page', {
                jobId: id,
                ...pageParams
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

    useWatch([pageParams], () => {
        getData();
    }, { immediate: true });

    useEffect(() => {
        getData();
    }, []);

    return (
        <div style={{
            marginTop: '20px',
        }}
        >
            <Table<TJobsInstanceTableItem>
                size="middle"
                loading={loading}
                rowKey="id"
                columns={columns}
                dataSource={tableData.list || []}
                onChange={onChange}
                pagination={{
                    size: 'small',
                    total: tableData.total,
                    showSizeChanger: true,
                    current: pageParams.pageNumber,
                    pageSize: pageParams.pageSize,
                }}
            />

            <RenderLoggerModal />
            <RenderErrorDataModal />
            <RenderResultModal />
        </div>

    );
};

export default forwardRef(Index);
