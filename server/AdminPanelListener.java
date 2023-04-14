public interface AdminPanelListener {
    void btnRecallClicked();

    void btnStopServerClicked();

    void btnChangeCordinatesClicked(int x, int y, int droneId);

    void btnDeleteFireReportClicked(int fireId);
}