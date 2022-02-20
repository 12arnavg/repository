import pandas
import plotly.express as express

diseases = ["smallpox", "pertussis", "polio", "measles", "mumps", "rubella", "hepatitis", "coronavirus"]

for i in range(len(diseases)):

  casesByState = {}

  file = open(diseases[i] + "CasesByState.csv", "r")
  year = file.readline().strip()

  for line in file:
    line = line.strip()
    (state, cases) = line.split(",")
    cases = int(cases)
    casesByState[state] = cases

  dataframe = pandas.DataFrame(list(casesByState.items()), columns = ['states','cases']) 
  dataframe["text"] = dataframe.apply(lambda x: '%s: %s cases' % (x['states'], format(x['cases'], ",")), axis = 1)

  fig = express.choropleth(dataframe, locations = "states",  color = "cases", color_continuous_scale = "YlOrRd", hover_name = "text", hover_data = {"states" : False, "cases" : False, "text" : False}, locationmode = "USA-states")
  fig.update_layout(title_text = diseases[i].capitalize() + " Cases By State In " + year, geo_scope = "usa")
  fig.show()