import React, {useEffect, useState} from 'react';
import {
    Tabs
} from "antd";
const { TabPane } = Tabs;

import {useIntl} from "react-intl";
import ExecutionDashboard from "view/Main/HomeDetail/Dashboard/executionDashboard";
import QualityReportDashboard from "view/Main/HomeDetail/Dashboard/qualityReportDashboard";

type TJobs = {
    datasourceId?: any,
}

const Dashboard = ({ datasourceId }: TJobs) => {

    const intl = useIntl();
    const [activeKey, setActiveKey] = useState('1');
    const match = useRouteMatch();

    const [entityParam, setEntityParam] = useState<any>({
        schemaName: null,
        tableName: null,
        columnName: null
    });
    const [pageParam, setPageParam] = useState<any>({
        pageNumber : 1,
        pageSize : 5
    });
    const [metricType, setMetricType] = useState<any>();
    const [startTime, setStartTime] = useState<any>();
    const [endTime, setEndTime] = useState<any>();

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
            } else if (value.length == 3) {
                setEntityParam({
                    schemaName : value[0],
                    tableName : value[1],
                    columnName : value[2]
                })
            }
        } else {
            setEntityParam({
                schemaName : null,
                tableName : null,
                columnName : null
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

                const columns = await $http.get(`/datasource/${(match.params as any).id}/${selectedOptions[0].value}/${selectedOptions[1].value}/columns`);
                let $reColumns = columns ? JSON.parse(JSON.stringify(columns)) : [];
                const $reColumns1: ((prevState: never[]) => never[]) | { value: any; label: any; isLeaf:any;}[] = [];
                $reColumns.forEach((item: { name: any; }) => {
                    $reColumns1.push({value: item.name, label: item.name, isLeaf:true})
                })
                targetOption.children = $reColumns1;
                setDataBases([...databases])
            }
        },1000);
    };

    const [loading, setLoading] = useState(false);

    const [tableData, setTableData] = useState<{ list: TJobsInstanceTableItem[], total: number}>({ list: [], total: 0 });

    const { Render: RenderErrorDataModal, show: showErrorDataModal } = useInstanceErrorDataModal({});
    const { Render: RenderResultModal, show: showResultModal } = useInstanceResult({});
    const { Render: RenderLoggerModal, show: showLoggerModal } = useLogger({});

    const [databases, setDataBases] = useState<Option[]>([]);
    const [metricList, setMetricList] = useState([]);

    const getJobExecutionData = async (pageParam1 :any) => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/execution/page', {
                schemaName : entityParam.schemaName,
                tableName : entityParam.tableName,
                columnName : entityParam.columnName,
                metricType : metricType,
                datasourceId : datasourceId || (match.params as any).id,
                pageNumber : pageParam1.pageNumber,
                pageSize : pageParam1.pageSize,
                status:  6,
                startTime : startTime,
                endTime : endTime
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
            const res = (await $http.post('/job/execution/agg-pie', {
                    schemaName : entityParam.schemaName,
                    tableName : entityParam.tableName,
                    columnName : entityParam.columnName,
                    metricType : metricType,
                    datasourceId : datasourceId || (match.params as any).id,
                    startTime : startTime,
                    endTime : endTime
                },
            )) || [];
            console.log("agg pie res : ", res)

            const pieOption = {
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    top: '5%',
                    left: 'center'
                },
                color: ['#ef6567', '#91cd77'],
                series: [
                    {
                        type: 'pie',
                        radius: ['40%', '70%'],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: 40,
                                fontWeight: 'bold'
                            }
                        },
                        labelLine: {
                            show: false
                        },
                        data: res
                    }
                ]
            };
            setDqPieOption(pieOption)

        } catch (error) {
        } finally {
            setLoading(false);
        }
    };

    const getJobExecutionTrendBar = async () => {
        try {
            setLoading(true);
            const res = (await $http.post('/job/execution/trend-bar', {
                    schemaName : entityParam.schemaName,
                    tableName : entityParam.tableName,
                    columnName : entityParam.columnName,
                    metricType : metricType,
                    datasourceId : datasourceId || (match.params as any).id,
                    startTime : startTime,
                    endTime : endTime
                },
            )) || [];
            console.log("agg bar res : ", res)

    return (
        <div >
            <Tabs
                tabPosition="top"
                activeKey={activeKey}
                onChange={(value:string) => {
                    setActiveKey(value);
                }}
                style={{ marginBottom: '0px',marginTop :'5px' }}
                className="dv-tab-list"
            >
                <TabPane tab={intl.formatMessage({ id: 'dashboard_execution' })} key="1">
                    <ExecutionDashboard datasourceId={datasourceId}/>
                </TabPane>

                <TabPane tab={intl.formatMessage({ id: 'dashboard_quality_report' })} key="2">
                    <QualityReportDashboard datasourceId={datasourceId}/>
                </TabPane>

            </Tabs>
        </div>
    );
};

export default Dashboard;
