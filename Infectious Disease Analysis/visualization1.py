from bokeh.plotting import figure, show
from bokeh.models import Legend

figure = figure(title = "Cases By Week During Disease Outbreaks", width = 600, height = 600, y_axis_type = "log", x_range = [1, 51], y_range = [1, 10**9])
figure.xaxis.axis_label = "Week"
figure.xaxis.axis_label_text_font_style = "normal"
figure.yaxis.axis_label = "Cases"
figure.yaxis.axis_label_text_font_style = "normal"

diseases = ["smallpox", "pertussis", "polio", "measles", "mumps", "rubella", "hepatitis", "coronavirus"]
colors = ["steelblue", "firebrick", "darkviolet", "darkkhaki", "darkturquoise", "darkgreen", "black", "orangered"]

for i in range(len(diseases)):
  
  casesByWeek = {}
  
  file = open(diseases[i] + "CasesByWeek.csv", "r")
  year = file.readline().strip()
  
  for line in file:
    line = line.strip()
    (week, cases) = line.split(",")
    week = int(week)
    cases = int(cases)
    casesByWeek[week] = cases
  
  figure.line(list(casesByWeek), list(casesByWeek.values()), line_color = colors[i], line_width = 3, legend = diseases[i].capitalize() + " (" + year + ")")

figure.legend.label_text_font_size = "8pt"
figure.legend.location = (370, 430)
show(figure)