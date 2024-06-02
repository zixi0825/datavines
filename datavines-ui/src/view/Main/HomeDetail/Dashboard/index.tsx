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
