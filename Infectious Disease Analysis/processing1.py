diseases = ["smallpox", "pertussis", "polio", "measles", "mumps", "rubella", "hepatitis"]
years = ["1930", "1941", "1952", "1958", "1968", "1969", "1971"]

for i in range(len(diseases)):
  casesByWeek = {}
  casesByState = {}

  file = open(diseases[i] + ".csv", "r")

  for line in file:
    line = line.strip()
    #week,state,state_name,disease,cases,incidence_per_capita
    (week, state, state_name, disease, cases, incidence_per_capita) = line.split(",")
    cases = int(cases)
    
    if week[:4] == years[i]:
      if week in casesByWeek:
        casesByWeek[week] += cases
      else:
        casesByWeek[week] = cases

    if week[:4] == years[i]:
      if state in casesByState:
        casesByState[state] += cases
      else:
        casesByState[state] = cases
  
  weeks = list(casesByWeek)
  states = list(casesByState)
  
  weeks.sort()
  states.sort()
    
  casesByWeekFile = open(diseases[i] + "CasesByWeek.csv", "w")
  casesByWeekFile.write(years[i] + "\n")
  for week in weeks:
    casesByWeekFile.write(str(int(week[4:])) + "," + str(casesByWeek[week]) + "\n")

  casesByStateFile = open(diseases[i] + "CasesByState.csv", "w")
  casesByStateFile.write(years[i] + "\n")
  for state in states:
    casesByStateFile.write(state + "," + str(casesByState[state]) + "\n")