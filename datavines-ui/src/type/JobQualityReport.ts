export type TJobQualityReportTableItem = {
    id: string | number,
    databaseName: string,
    tableName: string,
    columnName: string,
    score: number,
    reportDate: string,
}

export type TJobQualityReportTableData = {
    list: TJobQualityReportTableItem[],
    total: number
};
