export type TJobsInstanceTableItem = {
    id: string | number,
    jobType: string,
    name: string,
    status: string,
    updateTime: string,
}

export type TJobsInstanceTableData = {
    list: TJobsInstanceTableItem[],
    total: number
};
