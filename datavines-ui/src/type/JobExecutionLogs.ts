export type TJobExecutionLosTableItem = {
    id: string | number,
    jobType: string,
    name: string,
    status: string,
    updateTime: string,
}

export type TJobExecutionLosTableData = {
    list: TJobExecutionLosTableItem[],
    total: number
};
