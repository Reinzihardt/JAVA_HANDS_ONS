flowchart TD
    Start([Start Program]) --> Load[Load Sample Data<br/>6 patients + 7 appointments]
    Load --> Menu

    %% ===================== MAIN MENU =====================
    Menu[Show Main Menu<br/>1. Add Patient<br/>2. Search Patient<br/>3. Display Patients<br/>4. Book Appointment<br/>5. Display Schedule<br/>6. Cancel Appointment<br/>7. Daily Summary<br/>8. Exit]
    Menu --> Choice{Read Choice}

    Choice -->|1| AddPatient
    Choice -->|2| SearchPatient
    Choice -->|3| DisplayPatients
    Choice -->|4| BookAppointment
    Choice -->|5| DisplaySchedule
    Choice -->|6| CancelAppointment
    Choice -->|7| DailySummary
    Choice -->|8| Exit([Exit Program<br/>Thank you message])
    Choice -->|Invalid| Invalid[Show Invalid choice message]
    Invalid --> Pause
    Choice -->|Other| Pause

    %% ===================== 1. ADD PATIENT =====================
    AddPatient[Add Patient] --> FullCheck1{Patient list full?<br/>MAX = 50}
    FullCheck1 -->|Yes| FullMsg1[Cannot add – list full]
    FullMsg1 --> Pause
    FullCheck1 -->|No| NameLoop

    NameLoop[Ask Patient Name] --> NameCancel{cancel?}
    NameCancel -->|Yes| Pause
    NameCancel -->|No| NameValid{Valid name?<br/>2-30 chars, letters + . - ' }
    NameValid -->|No| NameErr[Invalid name message]
    NameErr --> NameLoop
    NameValid -->|Yes| NameExists{Name already exists?}
    NameExists -->|Yes| NameDup[Name already exists]
    NameDup --> NameLoop
    NameExists -->|No| AgeAsk

    AgeAsk[Ask Age 1-120] --> AgeCancel{cancel?}
    AgeCancel -->|Yes| Pause
    AgeCancel -->|No| ContactAsk

    ContactAsk[Ask Contact Number] --> ContactCancel{cancel?}
    ContactCancel -->|Yes| Pause
    ContactCancel -->|No| ContactValid{Valid contact?<br/>7-15 digits}
    ContactValid -->|No| ContactErr[Invalid contact]
    ContactErr --> ContactAsk
    ContactValid -->|Yes| ConcernAsk

    ConcernAsk[Show Concerns List<br/>1-9] --> ConcernCancel{cancel?}
    ConcernCancel -->|Yes| Pause
    ConcernCancel -->|No| ConcernOthers{Chose Others?}
    ConcernOthers -->|Yes| CustomConcern[Ask custom description<br/>1-30 chars]
    CustomConcern --> SavePatient
    ConcernOthers -->|No| SavePatient

    SavePatient[Generate ID P00x<br/>Save to parallel arrays<br/>patientCount++]
    SavePatient --> Success1[Patient added successfully<br/>Show assigned ID]
    Success1 --> Pause

    %% ===================== 2. SEARCH PATIENT =====================
    SearchPatient[Search Patient] --> SearchQuery[Ask Name or ID]
    SearchQuery --> SearchCancel{cancel?}
    SearchCancel -->|Yes| Pause
    SearchCancel -->|No| SearchLoop[Loop through patients]
    SearchLoop --> Found{Match found?}
    Found -->|Yes| ShowPatient[Display Patient Details]
    ShowPatient --> Pause
    Found -->|No| NotFound[Patient not found]
    NotFound --> Pause

    %% ===================== 3. DISPLAY PATIENTS =====================
    DisplayPatients[Display Patients] --> EmptyCheck{patientCount == 0?}
    EmptyCheck -->|Yes| NoPatients[No patient records found]
    NoPatients --> Pause
    EmptyCheck -->|No| PrintTable[Print formatted table<br/>ID / NAME / AGE / CONTACT / CONCERN]
    PrintTable --> Total[Show Total patients]
    Total --> Pause

    %% ===================== 4. BOOK APPOINTMENT =====================
    BookAppointment[Book Appointment] --> FullCheck2{Appointment list full?}
    FullCheck2 -->|Yes| FullMsg2[Cannot add – list full]
    FullMsg2 --> Pause
    FullCheck2 -->|No| NoPatientCheck{No patients yet?}
    NoPatientCheck -->|Yes| NoPatMsg[Add a patient first]
    NoPatMsg --> Pause
    NoPatientCheck -->|No| SelectPatient

    SelectPatient[askPatient - Find by Name/ID] --> PatCancel{cancel?}
    PatCancel -->|Yes| Pause
    PatCancel -->|No| DateAsk

    DateAsk[Ask Date YYYY-MM-DD] --> DateCancel{cancel?}
    DateCancel -->|Yes| Pause
    DateCancel -->|No| DentistAsk

    DentistAsk[Show 3 Dentists] --> DentistCancel{cancel?}
    DentistCancel -->|Yes| Pause
    DentistCancel -->|No| TimeSlots

    TimeSlots[Show TimeSlots<br/>Mark booked ones] --> OpenCheck{Any open slots?}
    OpenCheck -->|No| FullyBooked[Dentist fully booked]
    FullyBooked --> Pause
    OpenCheck -->|Yes| TimeAsk

    TimeAsk[Ask Slot number or time] --> TimeCancel{cancel?}
    TimeCancel -->|Yes| Pause
    TimeCancel -->|No| TimeValid{Valid + no conflict?}
    TimeValid -->|No| TimeErr[Invalid or conflict]
    TimeErr --> TimeAsk
    TimeValid -->|Yes| ProcAsk

    ProcAsk[Show Procedures<br/>with suggested one] --> ProcCancel{cancel?}
    ProcCancel -->|Yes| Pause
    ProcCancel -->|No| Confirm

    Confirm[Show summary<br/>Confirm Y/N?] --> ConfirmYes{Yes?}
    ConfirmYes -->|No| NotSaved[Booking not saved]
    NotSaved --> Pause
    ConfirmYes -->|Yes| SaveAppt[Generate A00x<br/>Save appointment<br/>Status = Scheduled]
    SaveAppt --> Success2[Appointment booked<br/>Show Appointment ID]
    Success2 --> Pause

    %% ===================== 5. DISPLAY SCHEDULE =====================
    DisplaySchedule[Display Schedule] --> SchedDate[Ask Date]
    SchedDate --> SchedCancel{cancel?}
    SchedCancel -->|Yes| Pause
    SchedCancel -->|No| PrintSched[Print schedule sorted by time<br/>ID TIME PATIENT DENTIST PROCEDURE FEE STATUS]
    PrintSched --> Pause

    %% ===================== 6. CANCEL APPOINTMENT =====================
    CancelAppointment[Cancel Appointment] --> ApptID[Ask Appointment ID]
    ApptID --> ApptCancel{cancel?}
    ApptCancel -->|Yes| Pause
    ApptCancel -->|No| FindAppt{Found?}
    FindAppt -->|No| NotFoundAppt[Appointment not found]
    NotFoundAppt --> ApptID
    FindAppt -->|Yes| AlreadyCancel{Already Cancelled?}
    AlreadyCancel -->|Yes| AlreadyMsg[Already cancelled]
    AlreadyMsg --> Pause
    AlreadyCancel -->|No| ConfirmCancel[Show details<br/>Confirm cancel Y/N?]
    ConfirmCancel --> CancelYes{Yes?}
    CancelYes -->|No| NotCancelled[Not cancelled]
    NotCancelled --> Pause
    CancelYes -->|Yes| SetCancel[Status = Cancelled]
    SetCancel --> CancelDone[Slot can now be rebooked]
    CancelDone --> Pause

    %% ===================== 7. DAILY SUMMARY =====================
    DailySummary[Daily Summary] --> SumDate[Ask Date]
    SumDate --> SumCancel{cancel?}
    SumCancel -->|Yes| Pause
    SumCancel -->|No| Calc[Calculate:<br/>Active / Cancelled / Fees<br/>Load per dentist]
    Calc --> PrintSum[Print Summary]
    PrintSum --> Pause

    %% ===================== PAUSE & LOOP =====================
    Pause[Press Enter to continue...] --> Menu

    style Start fill:#2e7d32,color:#fff
    style Exit fill:#c62828,color:#fff
    style Menu fill:#1565c0,color:#fff
    style Success1 fill:#2e7d32,color:#fff
    style Success2 fill:#2e7d32,color:#fff
    style SetCancel fill:#ef6c00,color:#fff
