import React, {useEffect, useState} from 'react';
import {
    Button,
    Cascader,
    Checkbox,
    Col,
    DatePicker, DatePickerProps,
    Dropdown,
    Form,
    Input,
    MenuProps,
    message, Modal,
    Row,
    Select,
    Table
} from "antd";
import DashBoard from "@Editor/components/Database/Detail/dashBoard";
import {Title} from "@/component";
import {useIntl} from "react-intl";
import * as echarts from 'echarts';
import {useRouteMatch} from "react-router-dom";
import {$http} from "@/http";
import {useMount} from "@Editor/common";
import {ColumnsType} from "antd/lib/table";

import {defaultRender} from "utils/helper";
import {qualityReportDetailModal} from "view/Main/HomeDetail/Dashboard/qualityReportDashboard/qualityReportDetailModal";
import { useWatch } from '@/common';
import {TJobQualityReportTableItem} from "@/type/JobQualityReport";
import MetaDataFetcher from "view/Main/Home/List/MetaDataFetcher";
import ReportScheduler from "view/Main/HomeDetail/Dashboard/qualityReportDashboard/ReportScheduler";

type TJobs = {
    datasourceId?: any,
}

interface Option {
    value?: string | number | null;
    label: React.ReactNode;
    children?: Option[];
    isLeaf?: boolean;
}

const app: any = {};
const posList = [
    'left',
    'right',
    'top',
    'bottom',
    'inside',
    'insideTop',
    'insideLeft',
    'insideRight',
    'insideBottom',
    'insideTopLeft',
    'insideTopRight',
    'insideBottomLeft',
    'insideBottomRight'
] as const;

app.configParameters = {
    rotate: {
        min: -90,
        max: 90
    },
    align: {
        options: {
            left: 'left',
            center: 'center',
            right: 'right'
        }
    },
    verticalAlign: {
        options: {
            top: 'top',
            middle: 'middle',
            bottom: 'bottom'
        }
    },
    position: {
        options: posList.reduce(function (map, pos) {
            map[pos] = pos;
            return map;
        }, {} as Record<string, string>)
    },
    distance: {
        min: 0,
        max: 100
    }
};

const QualityReportDashboard = ({ datasourceId }: TJobs) => {
    const [dqBarOption, setDqBarOption] = useState<any>();
    const [dqGaugeOption, setDqGaugeOption] = useState<any>();

    const intl = useIntl();
    const match = useRouteMatch();

    const [entityParam, setEntityParam] = useState<any>({
        schemaName: null,
        tableName: null
    });

    const [pageParam, setPageParam] = useState<any>({
        pageNumber : 1,
        pageSize : 5
    });

    const [startTime, setStartTime] = useState<any>();
    const [endTime, setEndTime] = useState<any>();

    const [reportDate, setReportDate] = useState<any>();

    const onEntitySelectChange = async (value: (string | number | null)[], selectedOptions: Option[]) => {

        if (value) {
            if (value.length == 1) {
                setEntityParam({
                    schemaName:value[0]
                })

            } else if (value.length == 2) {
                setEntityParam({
                    schemaName : value[0],
                    tableName : value[1]
                })
            }
        } else {
            setEntityParam({
                schemaName : null,
                tableName : null
            })
        }
    };

    const loadData = (selectedOptions: Option[]) => {
        console.log(selectedOptions);
        setTimeout(async () => {
            const targetOption = selectedOptions[selectedOptions.length - 1];
            if (selectedOptions.length == 1) {
                const tables = await $http.get(`/datasource/${(match.params as any).id}/${selectedOptions[0].value}/tables`);
                let $reTables = tables ? JSON.parse(JSON.stringify(tables)) : [];
                const $reTables1: ((prevState: never[]) => never[]) | { value: any; label: any; isLeaf:any;}[] = [];
                $reTables.forEach((item: { name: any; }) => {
                    $reTables1.push({value: item.name, label: item.name,isLeaf:false})
                })
                targetOption.children = $reTables1;
                setDataBases([...databases])
            } else if (selectedOptions.length == 2) {
                targetOption.children = [];
                setDataBases([...databases])
            }
        },1000);
    };

    const [loading, setLoading] = useState(false);

    const [tableData, setTableData] = useState<{ list: TJobQualityReportTableItem[], total: number}>({ list: [], total: 0 });

    const { Render: RenderQualityReportDetailModal, show: showQualityReportDetailModal } = qualityReportDetailModal({});

    const [databases, setDataBases] = useState<Option[]>([]);

    const [isScheduleOpen, setisScheduleOpen] = useState(false);

    const [refresh, setRefresh] = useState(0);

    const getJobExecutionData = async (pageParam1 :any) => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/quality-report/page', {
                schemaName : entityParam.schemaName,
                tableName : entityParam.tableName,
                datasourceId : datasourceId || (match.params as any).id,
                reportDate: reportDate,
                pageNumber : pageParam1.pageNumber,
                pageSize : pageParam1.pageSize
                },
            )) || [];
            setTableData({
                list: res?.records || [],
                total: res.total || 0,
            });
        } catch (error) {
        } finally {
            setLoading(false);
        }
    };

    const getJobExecutionAggPie = async () => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/quality-report/score', {
                    schemaName : entityParam.schemaName,
                    tableName : entityParam.tableName,
                    datasourceId : datasourceId || (match.params as any).id,
                    reportDate : reportDate
                },
            )) || [];
            console.log("quality score res : ", res)

            const gaugeOption = {
                series: [
                    {
                        type: 'gauge',
                        startAngle: 180,
                        endAngle: 0,
                        center: ['50%', '75%'],
                        radius: '90%',
                        min: 0,
                        max: 1,
                        splitNumber: 8,
                        axisLine: {
                            lineStyle: {
                                width: 6,
                                color: [
                                    [0.2, '#fd0513'],
                                    [0.4, '#e3622b'],
                                    [0.6, '#FDDD60'],
                                    [0.8, '#58D9F9'],
                                    [1, '#7CFFB2']
                                ]
                            }
                        },
                        pointer: {
                            icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                            length: '12%',
                            width: 20,
                            offsetCenter: [0, '-60%'],
                            itemStyle: {
                                color: 'auto'
                            }
                        },
                        axisTick: {
                            length: 12,
                            lineStyle: {
                                color: 'auto',
                                width: 2
                            }
                        },
                        splitLine: {
                            length: 20,
                            lineStyle: {
                                color: 'auto',
                                width: 5
                            }
                        },
                        axisLabel: {
                            color: '#464646',
                            fontSize: 20,
                            distance: -60,
                            rotate: 'tangential',
                            formatter: function (value: number) {
                                return '';
                            }
                        },
                        title: {
                            offsetCenter: [0, '-10%'],
                            fontSize: 20
                        },
                        detail: {
                            fontSize: 30,
                            offsetCenter: [0, '-35%'],
                            valueAnimation: true,
                            formatter: function (value: number) {
                                return Math.round(value * 100) + '';
                            },
                            color: 'inherit'
                        },
                        data: [
                            {
                                value: res.score / 100,
                                name: res.qualityLevel
                            }
                        ]
                    }
                ]
            };
            setDqGaugeOption(gaugeOption)

        } catch (error) {
        } finally {
            setLoading(false);
        }
    };

    const getJobExecutionTrendBar = async () => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/quality-report/score-trend', {
                    schemaName : entityParam.schemaName,
                    tableName : entityParam.tableName,
                    datasourceId : datasourceId || (match.params as any).id,
                    reportDate : reportDate
                },
            )) || [];
            console.log("score trend res : ", res)

            const barOption = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {},
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: res.dateList
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                series: [
                    {
                        name: 'Score',
                        type: 'line',
                        data: res.scoreList,
                        markPoint: {
                            data: [
                                { type: 'max', name: 'Max' },
                                { type: 'min', name: 'Min' }
                            ]
                        },
                        markLine: {
                            data: [{ type: 'average', name: 'Avg' }]
                        }
                    }
                ]
            };
            setDqBarOption(barOption)

        } catch (error) {
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if(isScheduleOpen) {
            setRefresh(prev => prev + 1);
        }
    }, [isScheduleOpen])

    useWatch([(match.params as any).id], () =>{
        refreshData()
    })

    useMount(async () => {
        refreshData()
    });

    const refreshData = async () => {
        setEntityParam({
            schemaName: null,
            tableName: null
        })

        setStartTime(null)
        setEndTime(null)

        const $datasourceId = datasourceId || (match.params as any).id
        const $databases = await $http.get(`/datasource/${$datasourceId}/databases`);
        let $reDatabases = $databases ? JSON.parse(JSON.stringify($databases)) : [];
        const $reDatabases1: ((prevState: never[]) => never[]) | { value: any; label: any; isLeaf: any; }[] = [];
        $reDatabases.forEach((item: { name: any; }) => {
            return $reDatabases1.push({value: item.name, label: item.name, isLeaf: false});
        })
        // @ts-ignore
        setDataBases($reDatabases1);
        getJobExecutionData(pageParam);
        getJobExecutionAggPie();
        getJobExecutionTrendBar();
    }

    const onPageChange = ({ current, pageSize }: any) => {
        setPageParam({
            pageNumber : current,
            pageSize : pageSize
        })

        getJobExecutionData({
            pageNumber : current,
            pageSize : pageSize
        });
    };

    const onShow = (record: TJobQualityReportTableItem) => {
        showQualityReportDetailModal(record);
    };

    const onStartTimeChange : DatePickerProps['onChange'] = (date, dateString) => {
        setReportDate(dateString)
    };

    const onEndTimeChange : DatePickerProps['onChange'] = (date, dateString) => {
        setEndTime(dateString)
    };

    const onQueryClick = () => {
        getJobExecutionData(pageParam);
        getJobExecutionAggPie();
        getJobExecutionTrendBar();
    }

    const [uuid, setUuid] = useState<string | number>('');
    const showSchedule = () => {
        setisScheduleOpen(true);
        setUuid(datasourceId || (match.params as any).id);
    }

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
        },
        {
            title: intl.formatMessage({ id: 'common_action' }),
            fixed: 'right',
            key: 'right',
            dataIndex: 'right',
            width: 200,
            render: (text: string, record: TJobQualityReportTableItem) => (
                <>
                    <a style={{ marginRight: 5 }} onClick={() => { onShow(record); }}>{intl.formatMessage({ id: 'jobs_task_report_detail' })}</a>
                </>
            ),
        },
    ];
    return (
        <div className="dv-page-padding" style={{height:'calc(100vh - 140px)'}} >
            <div>
                <Row style = {{marginTop: '0px'}}>
                    <Col span={24}>
                        <Cascader options={databases} loadData={loadData} onChange={onEntitySelectChange} changeOnSelect style = {{width:500}}/>
                        <span style = {{marginLeft: '20px'}}>报告时间</span> <DatePicker onChange={onStartTimeChange} style = {{marginLeft: '10px'}} ></DatePicker>
                        <Button style = {{marginLeft: '20px'}} onClick={onQueryClick} >查询</Button>
                        <Button style = {{marginLeft: '20px'}} onClick={showSchedule} >定时配置</Button>
                    </Col>
                </Row>
            </div>
            <div style = {{height:'calc(100vh - 160px)',overflow: 'auto'}}>
                <Row style = {{marginTop: '20px'}}>
                    <Col span={12}>
                        <Title>
                            {intl.formatMessage({ id: 'quality_report_dashboard_score' })}
                        </Title>
                        <DashBoard option={dqGaugeOption} id={"3"} style={{height:'350px',width:'calc(50vw - 100px)'}}/>
                    </Col>
                    <Col span={12}>
                        <Title>
                            {intl.formatMessage({ id: 'quality_report_dashboard_trend' })}
                        </Title>
                        <DashBoard option={dqBarOption} id={"4"} style={{height:'350px',width:'calc(50vw - 100px)'}}/>
                    </Col>
                </Row>
                <Row>
                    <Col span={24} >
                        <Title>
                            {intl.formatMessage({ id: 'quality_report_dashboard_detail' })}
                        </Title>
                        <Table<TJobQualityReportTableItem>
                            size="middle"
                            loading={loading}
                            rowKey="id"
                            columns={columns}
                            dataSource={tableData.list || []}
                            onChange={onPageChange}
                            pagination={{
                                size: 'small',
                                total: tableData.total,
                                showSizeChanger: true,
                                current: pageParam.pageNumber,
                                pageSize: pageParam.pageSize,
                                pageSizeOptions: [5, 10, 20, 50, 100],
                            }}
                        />
                        <RenderQualityReportDetailModal />
                    </Col>
                </Row>
            </div>
            <Modal
                width="1500px"
                footer={[]}
                title={intl.formatMessage({ id: 'quality_report_schedule' })}
                onCancel={() => {
                    setisScheduleOpen(false);
                }}
                open={isScheduleOpen}
                maskClosable={false}
            >
                <ReportScheduler
                    datasourceId={uuid}
                    refreshKey={refresh}
                    onSavaEnd={() => {
                        setisScheduleOpen(false);
                    }}
                />
            </Modal>
        </div>
    );
};

export default QualityReportDashboard;
