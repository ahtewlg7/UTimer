package ahtewlg7.utimer.log;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.CoreConfiguration;
import org.acra.data.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.sender.ReportSenderFactory;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.material.LogAttachFile;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.FileIOAction;

/**
 * Created by lw on 2019/6/9.
 */
public class AcraReportSenderFactory implements ReportSenderFactory {
    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull CoreConfiguration config) {
        return new AcraReportSender();
    }
    class AcraReportSender implements ReportSender{
        @Override
        public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
            toWriteChrashInfo(errorContent);
        }
    }

    private void toWriteChrashInfo(CrashReportData errorContent){
        try{
            String logDir  = new FileSystemAction().getLogDataAbsPath();
            String logFile = new DateTimeAction().toFormatNow();
            LogAttachFile file = new LogAttachFile(logDir, logFile);
            new FileIOAction(file).getCharWriter().write(errorContent.toJSON());
        }catch (Exception exe){
            exe.printStackTrace();
        }
    }
}
