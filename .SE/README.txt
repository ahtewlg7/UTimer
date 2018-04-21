1. the Architecture of the UTimer

                        |
              UI        |                    activity / service
                        |
        ----------------|------------------------------------------------------------
                        |
                        |                           View
                        |
                        |
              MVP       |                         Presenter
                        |
                        |
                        |                           Model
                        |
        ----------------|------------------------------------------------------------
                        |
           Application  |                          Facade
                        |
        ----------------|------------------------------------------------------------
                        |
             Domain     |                        Logic Manager
                        |
        ----------------|------------------------------------------------------------
                        |
           Technology   |                          ***Action
                        |
        ----------------|------------------------------------------------------------
                        |
           Persistence  |                         DB / File
                        |


2.The service of UI is used to :
    to share data with multi-activity;
    to keep working when the UI lifeCycle is over;

3.