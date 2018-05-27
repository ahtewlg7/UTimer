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
            (MVVM)      |
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
            (Mapper)    |                 (Domain Entity / Db Bean Mapper)
        ----------------|------------------------------------------------------------
                        |
           Persistence  |                       DB / Serialized LOB
                        |


2.the Gtd State Machine

              trash <--------- note ---------> rely ( gtdEntity / otherOne )
                                |
                                | (to gtd)
                                |
                             project ---------> actionNum ( inbox = 1  /  project > 1 )
                                |
                                | (to work)
                                |
plan action list ---------> next Action ---------> do it
                                                     |
                                                     |
                                                     |
                                             ------------------
                                             |                |
                                             |                |
                                         by myself         delegate ---------> wait for someOne
                                             |
                                             |
                                     ------------------
                                     |                |
                                     |                |
                                    now             defer
                                                      |
                                                      |
                                              ------------------
                                              |                |
                                              |                |
                                           calendar        to do list

3.The service of UI is used to :
      to share data with multi-activity;
      to keep working when the UI lifeCycle is over;