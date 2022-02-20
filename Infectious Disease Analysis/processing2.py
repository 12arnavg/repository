import datetime

casesByWeek = {}
casesByState = {}

file = open("coronavirus.csv", "r")

for line in file:
  line = line.strip()
  #date,state,death,deathConfirmed,deathIncrease,deathProbable,hospitalized,hospitalizedCumulative,hospitalizedCurrently,hospitalizedIncrease,inIcuCumulative,inIcuCurrently,negative,negativeIncrease,negativeTestsAntibody,negativeTestsPeopleAntibody,negativeTestsViral,onVentilatorCumulative,onVentilatorCurrently,positive,positiveCasesViral,positiveIncrease,positiveScore,positiveTestsAntibody,positiveTestsAntigen,positiveTestsPeopleAntibody,positiveTestsPeopleAntigen,positiveTestsViral,recovered,totalTestEncountersViral,totalTestEncountersViralIncrease,totalTestResults,totalTestResultsIncrease,totalTestsAntibody,totalTestsAntigen,totalTestsPeopleAntibody,totalTestsPeopleAntigen,totalTestsPeopleViral,totalTestsPeopleViralIncrease,totalTestsViral,totalTestsViralIncrease
  (date, state, death, deathConfirmed, deathIncrease, deathProbable, hospitalized, hospitalizedCumulative, hospitalizedCurrently, hospitalizedIncrease, inIcuCumulative, inIcuCurrently, negative, negativeIncrease, negativeTestsAntibody, negativeTestsPeopleAntibody, negativeTestsViral, onVentilatorCumulative, onVentilatorCurrently, positive, positiveCasesViral, positiveIncrease, positiveScore, positiveTestsAntibody, positiveTestsAntigen,positiveTestsPeopleAntibody, positiveTestsPeopleAntigen, positiveTestsViral, recovered, totalTestEncountersViral, totalTestEncountersViralIncrease, totalTestResults, totalTestResultsIncrease, totalTestsAntibody, totalTestsAntigen, totalTestsPeopleAntibody, totalTestsPeopleAntigen, totalTestsPeopleViral, totalTestsPeopleViralIncrease, totalTestsViral, totalTestsViralIncrease) = line.split(",")
  date = date[1:][:-1]
  state = state[1:][:-1]
  (year, month, day) = date.split("-")

  if year == "2020" and not positiveIncrease == "":
    week = datetime.date(int(year), int(month), int(day)).isocalendar()[1]
    positiveIncrease = int(positiveIncrease)

    if week in casesByWeek:
      casesByWeek[week] += positiveIncrease
    else:
      casesByWeek[week] = positiveIncrease

    if state in casesByState:
      casesByState[state] += positiveIncrease
    else:
      casesByState[state] = positiveIncrease

weeks = list(casesByWeek)
states = list(casesByState)

weeks.sort()
states.sort()

casesByWeekFile = open("coronavirusCasesByWeek.csv", "w")
casesByWeekFile.write("2020" + "\n")
for week in weeks:
  casesByWeekFile.write(str(week) + "," + str(casesByWeek[week]) + "\n")

casesByStateFile = open("coronavirusCasesByState.csv", "w")
casesByStateFile.write("2020" + "\n")
for state in states:
  casesByStateFile.write(state + "," + str(casesByState[state]) + "\n")