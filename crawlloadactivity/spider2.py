import requests
import re
import numpy as np
import datetime
import json
from bs4 import BeautifulSoup

def parse(html):
	mysoup = BeautifulSoup(html, 'lxml')
	course_list = mysoup.find_all('td')
	cn = []
	ccrnn = []
	ctitle = []
	for c in course_list:
		reg1 = re.compile(r"top: \d+.\dpt; left: 136.8pt; margin-top:0pt;")
		reg2 = re.compile(r"top: \d+.\dpt; left: 0.0pt; margin-top:0pt;")
		r1 = c.find('span', attrs={'class':'f0', 'style':reg1})
		r2 = c.find('span', attrs={'class':'f0', 'style':reg2})
		if r1:
			r11 = r1.getText()
			cn.append(r11)
		if r2:
			r22 = r2.getText()
			pos = r22.rfind('-')
			new_r22 = r22[0:pos]
			(c, t) = new_r22.split(' ')
			ccrnn.append(c)
			ctitle.append(t)
	ctitle_list = [ctitle[0]]
	rep = []
	i = 1
	while i < len(ctitle):
		if ctitle[i] == ctitle_list[-1]:
			rep.append(i)
			i += 1
			continue
		ctitle_list.append(ctitle[i])
		i += 1
	cn_list = []
	j = 0
	while j < len(cn):
		if j in rep:
			j += 1
			continue
		cn_list.append(cn[j])
		j += 1
	return cn_list, ccrnn, ctitle_list

def write(year, cn, ccrnn, ctitle):
	file1 = str(year) + '_course_title.json'
	with open(file1, 'w') as file_object:
		json.dump(ctitle_list, file_object)
	file2 = str(year) + '_course_name.json'
	with open(file2, 'w') as file_object:
		json.dump(cn, file_object)
	file3 = str(year) + '_course_crn.json'
	with open(file3, 'w') as file_object:
		json.dump(ccrnn, file_object)



if __name__ == '__main__':
	now = datetime.datetime.today()
	now_time = str(now)
	y_pos = now_time.find('-')
	year_s = now_time[0:y_pos]
	year = int(year_s)
	remain = now_time[y_pos + 1:]
	y_mon = remain.find('-')
	month = remain[0:y_mon]
	semester = '09'
	if int(month) <= 6:
		semester = '01'
	while True:
		header = 'https://sis.rpi.edu/reg/zs'
		tail = '.htm'
		url = header + str(year) + semester + tail
		# url = 'https://sis.rpi.edu/reg/zs201809.htm'
		he = requests.head(url)
		status = he.status_code
		res = requests.get(url)
		if status != 404:
			html = res.text
			cn, ccrnn, ctitle_list = parse(html)
			write(year, cn, ccrnn, ctitle_list)
		else:
			break
		year += 1
		if semester == '09':
			semester = '01'
		elif semester == '01':
			semester = '09'


# # #coding: utf-8
# # #!/usr/bin/env python3
# # from selenium import webdriver
# # from selenium.webdriver.firefox.options import Options

# # # chrome_options = webdriver.ChromeOptions()
# # # chrome_options.add_argument('--no-sandbox')
# # # chrome_options.add_argument('--headless')
# # # url = 'https://sis.rpi.edu/rss/twbkwbis.P_WWWLogin'
# # # browser = webdriver.Chrome()
# # # browser.get(url)
# # # print (browser.page_source)

# # url = 'https://sis.rpi.edu/rss/twbkwbis.P_WWWLogin'
# # url1 = 'www.baidu.com'

# # # options = webdriver.FirefoxOptions()
# # # options.add_argument('-headless')
# # # driver = webdriver.Firefox(firefox_options=options)
# # options = Options()
# # options.headless = True
# # driver = webdriver.Firefox(options=options)
# # browser.get(url1)
# # print (browser.page_source)

# #==============================================================

# # import requests

# # userAgent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763'
# # header = {
# # 	'Referer':'https://sis.rpi.edu/rss/twbkwbis.P_ValLogin',
# # 	'User-Agent':userAgent
# # }

# # def Login():
# # 	print ("start")
# # 	url = 'https://sis.rpi.edu/rss/twbkwbis.P_WWWLogin'
# # 	post_data = {
# # 		'sid':'660286461',
# # 		'PIN':'Kk1317111317',
# # 	}
# # 	responseRes = requests.post(url, data = post_data, headers = header)
# # 	print(responseRes.text)

# # if __name__ == '__main__':
# # 	Login()



# # import requests

# # userAgent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763'
# # header = {
# # 	'Referer':'https://submitty.cs.rpi.edu/authentication/login',
# # 	'User-Agent':userAgent
# # }

# # def Login():
# # 	print ("start")
# # 	url = 'https://submitty.cs.rpi.edu/authentication/login'
# # 	post_data = {
# # 		'user_id':'zhouy16',
# # 		'password':'W,vLsn+K',
# # 	}
# # 	responseRes = requests.post(url, data = post_data, headers = header)
# # 	print(responseRes.text)

# # if __name__ == '__main__':
# # 	Login()

# #==============================================================
# # import json
# # import scrapy
# # from scrapy import signals
# # import numpy as np

# # class TestSpider(scrapy.Spider):
# # 	# name = 'sislogin'
# # 	# start_urls = ['https://sis.rpi.edu/rss/twbkwbis.P_WWWLogin']
	
# # 	# @classmethod
# # 	# def from_crawler(cls, crawler, *args, **kwargs):
# # 	# 	spider = super(TestSpider, cls).from_crawler(crawler, *args, **kwargs)
# # 	# 	crawler.signals.connect(spider.spider_idle, signal=signals.spider_idle)
# # 	# 	return spider

# # 	# def parse(self, response):
# # 	# 	return scrapy.FormRequest.from_response(
# # 	# 			response, 
# # 	# 			formdata={'sid':'660286461','PIN':'Kk1317111317'}
# # 	# 			callback = self.logged_in
# # 	# 		)

# # 	# def logged_in(self, response):
# # 	# 	item = SampleItem()
# # 	# 	item['message'] = response.css('h2::text').extract_first()
# # 	# 	return item
# # 	name = 'baidu.com'
# # 	allowed_domain = ['www.baidu.com']
# # 	start_urls = ['https://www.baidu.com/']

# # 	def parse(self, response):
# # 		yield {
# # 			'title':response.xpath('//title/text()').extract_first()
# # 		}




# import json
# import scrapy
# from scrapy import signals
# import numpy as np

# class TestSpider(scrapy.Spider):
# 	name = 'spidersis'
# 	# # allowed_domain = ['https://sis.rpi.edu/rss/bwskfcls.p_sel_crse_search']
# 	# start_urls = ['https://sis.rpi.edu/rss/bwskfcls.p_sel_crse_search']

# 	# def parse(self, response):
# 	# 	yield {
# 	# 		'title':response.xpath('//title/text()').extract_first()
# 	# 	}

# 	def start_requests(self):
# 		url = 'https://sis.rpi.edu/rss/twbkwbis.P_WWWLogin'
# 		yield srcapy.FormRequest(
# 				url = url,
# 				formdata = {'sid' : '660286461', 'PIN' : 'Kk1317111317'},
# 				callback = self.parse_page
# 			)
# 	def parse_page(self, response):
# 		print (response.text)