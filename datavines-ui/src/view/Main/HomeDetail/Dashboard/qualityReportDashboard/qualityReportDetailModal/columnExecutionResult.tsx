/* eslint-disable react/no-danger */
import React, { useState } from 'react';
import { Table } from 'antd';
import { ColumnsType } from 'antd/lib/table';
import {
    useWatch,
} from 'src/common';
import { useIntl } from 'react-intl';
import { $http } from '@/http';
import { defaultRender } from '@/utils/helper';
import {TJobQualityReportTableItem} from "@/type/JobQualityReport";

type InnerProps = {
    [key: string]: any
}

const Index = (props: InnerProps) => {
    const intl = useIntl();
    const [loading, setLoading] = useState(false);
    const [tableData, setTableData] = useState<{ list: TJobQualityReportTableItem[], total: number}>({ list: [], total: 0 });
    const [pageParams, setPageParams] = useState({
        pageNumber: 1,
        pageSize: 10,
    });
    const columns: ColumnsType<TJobQualityReportTableItem> = [
        {
            title: intl.formatMessage({ id: 'jobs_task_metric_type' }),
            dataIndex: 'metricName',
            key: 'metricName',
            width: 200,
            render: (text: string) => defaultRender(text, 300),
        },
        {
            title: intl.formatMessage({ id: 'jobs_task_check_formula' }),
            dataIndex: 'resultFormulaFormat',
            key: 'resultFormulaFormat',
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
            title: intl.formatMessage({ id: 'jobs_task_check_result' }),
            dataIndex: 'checkResult',
            key: 'checkResult',
            width: 200,
            render: (text: string) => defaultRender(text, 300),
        },{
            title: intl.formatMessage({ id: 'jobs_task_last_time' }),
            dataIndex: 'executionTime',
            key: 'executionTime',
            width: 200,
            render: (text: string) => defaultRender(text, 300),
        }
    ];
    const getData = async () => {
        try {
            setLoading(true);
            const res = (await $http.get('/job/quality-report/listColumnExecution', {
                reportId: props.record.id
            })) || [];
            setTableData({
                list: res || [],
                total:  0,
            });
        } catch (error) {
        } finally {
            setLoading(false);
        }
    };
    useWatch([], async () => {
        getData();
    }, { immediate: true });
    // @ts-ignore
    return (
        <div style={{marginLeft: '60px'}}>
            <Table<TJobQualityReportTableItem>
                size="small"
                rowKey="id"
                columns={columns}
                dataSource={tableData.list || []}
                pagination={false}
            />
        </div>
    );
};

export default Index;
