from selenium import webdriver
from selenium.webdriver.firefox.options import Options
from time import sleep
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
import json
from bs4 import BeautifulSoup

url = 'https://sis.rpi.edu/rss/twbkwbis.P_WWWLogin'

options = Options()
options.headless = True
browser = webdriver.Firefox(options=options)
browser.get(url)
user = browser.find_element_by_id("UserID")
user.send_keys("")
browser.implicitly_wait(5)
pw = browser.find_element_by_name("PIN")
pw.send_keys("")
browser.implicitly_wait(5)
browser.find_element_by_xpath("//input[@type='submit' and @value='Login']").click()

browser.find_element_by_link_text("Student Menu").click()
browser.find_element_by_link_text("Class Search").click()

s = Select(browser.find_element_by_id("term_input_id"))
s.select_by_value("201909")
browser.find_element_by_xpath("//input[@type='submit' and @value='Submit']").click()


subj_crses = []
title = []
num = len(browser.find_elements_by_tag_name('option'))

for idx in range(0, num):
	print (idx)
	subj_n = ''
	crse_n = ''
	subj_crse = ''
	subj_idx = 0
	crse_idx = 0
	title_idx = 0
	s1 = Select(browser.find_element_by_id("subj_id"))
	s1.select_by_index(idx)
	browser.find_element_by_xpath("//input[@type='submit' and @value='Section Search']").click()
	if idx == 0:
		find_idx = -1
		for index_info in browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name('th'):
			if index_info.text == 'Subj':
				subj_idx = find_idx
			elif index_info.text == 'Crse':
				crse_idx = find_idx
			elif index_info.text == 'Title':
				title_idx = find_idx
			find_idx += 1
	numm = len(browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name('tr'))
	for i in range(3, numm):
		path1 = ".//table[@class='datadisplaytable']/tbody/tr[" + str(i) + "]/td[" + str(subj_idx + 1) + "]"
		path2 = ".//table[@class='datadisplaytable']/tbody/tr[" + str(i) + "]/td[" + str(crse_idx + 1) + "]"
		path3 = ".//table[@class='datadisplaytable']/tbody/tr[" + str(i) + "]/td[" + str(title_idx + 1) + "]"
		subj_n = browser.find_element_by_xpath(path1).text
		crse_n = browser.find_element_by_xpath(path2).text
		title_n = browser.find_element_by_xpath(path3).text
		subj_crse = subj_n + crse_n
		subj_crses.append(subj_crse)
		title.append(title_n)
	browser.find_element_by_xpath("//input[@type='submit' and @value='New Search']").click()
	s2 = Select(browser.find_element_by_id("term_input_id"))
	s2.select_by_value("201909")
	browser.find_element_by_xpath("//input[@type='submit' and @value='Submit']").click()


subj_n = ''
crse_n = ''
subj_crse = ''
s1 = Select(browser.find_element_by_id("subj_id"))
s1.select_by_index(3)
browser.find_element_by_xpath("//input[@type='submit' and @value='Section Search']").click()
find_idx = -1
for index_info in browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name('th'):
	if index_info.text == 'Subj':
		subj_idx = find_idx
	elif index_info.text == 'Crse':
		crse_idx = find_idx
	elif index_info.text == 'Title':
		title_idx = find_idx
	find_idx += 1
numm = len(browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name('tr'))
for i in range(3, numm):
	path1 = ".//table[@class='datadisplaytable']/tbody/tr[" + str(i) + "]/td[" + str(subj_idx + 1) + "]"
	path2 = ".//table[@class='datadisplaytable']/tbody/tr[" + str(i) + "]/td[" + str(crse_idx + 1) + "]"
	path3 = ".//table[@class='datadisplaytable']/tbody/tr[" + str(i) + "]/td[" + str(title_idx + 1) + "]"
	t1 = browser.find_element_by_xpath(path1)
	t2 = browser.find_element_by_xpath(path2)
	t3 = browser.find_element_by_xpath(path3)


# trlist = browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name("tr")
# for tr in trlist:
# 	t = tr.find_element_by_xpath(".//table[@class='datadisplaytable']/tbody/tr[3][td[]]")

# for idx in range(0, num):
# 	print (idx)
# 	subj_n = ''
# 	crse_n = ''
# 	subj_crse = ''
# 	s1 = Select(browser.find_element_by_id("subj_id"))
# 	s1.select_by_index(idx)
# 	browser.find_element_by_xpath("//input[@type='submit' and @value='Section Search']").click()
# 	if idx == 0:
# 		find_idx = -1
# 		for index_info in browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name('th'):
# 			if index_info.text == 'Subj':
# 				subj_idx = find_idx
# 			elif index_info.text == 'Crse':
# 				crse_idx = find_idx
# 			elif index_info.text == 'Title':
# 				title_idx = find_idx
# 			find_idx += 1
# 	trlist = browser.find_element_by_xpath("//table[@class='datadisplaytable']").find_elements_by_tag_name("tr")
# 	for tr in trlist:
# 		tdlist = tr.find_elements_by_tag_name("td")
# 		i = 0
# 		for td in tdlist:
# 			if i == subj_idx:
# 				subj_n = td.text
# 			elif i == crse_idx:
# 				crse_n = td.text
# 				subj_crse = subj_n + crse_n
# 				subj_crses.append(subj_crse)
# 			elif i == title_idx:
# 				title.append(td.text)
# 			i += 1
# 	browser.find_element_by_xpath("//input[@type='submit' and @value='New Search']").click()
# 	s2 = Select(browser.find_element_by_id("term_input_id"))
# 	s2.select_by_value("201909")
# 	browser.find_element_by_xpath("//input[@type='submit' and @value='Submit']").click()


title_set = list(set(title))
title_set.sort(key=title.index)
title_list = list(title_set)
sub_set = list(set(subj_crses))
sub_set.sort(key=subj_crses.index)
sub_list = list(sub_set)
browser.close()