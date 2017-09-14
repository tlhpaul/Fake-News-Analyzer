import tweepy 
import csv
import sys


consumer_key = ""
consumer_secret = ""
access_key = ""
access_secret = ""

def get_all_tweets(screen_name):
        auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
        auth.set_access_token(access_key, access_secret)
        api = tweepy.API(auth)
        alltweets = []
        new_tweets = api.user_timeline(screen_name = screen_name,count=200)
        alltweets.extend(new_tweets)
        oldest = alltweets[-1].id - 1
        while len(new_tweets) > 0:
                new_tweets = api.user_timeline(screen_name = screen_name,count=200,max_id=oldest)
                alltweets.extend(new_tweets)
                oldest = alltweets[-1].id - 1
         
        outtweets = [] 
        for tweet in alltweets:
                date = str(tweet.created_at)
                title = tweet.text.encode("utf-8")
                if screen_name == 'realdonaldtrump':
                        title = title.replace('I ', ' Trump ')
                        title = title.replace(' me ', ' Trump ')
                        title = title.replace(' I ', ' Trump ')
                if '2017-04' in date or '2017-03' in date: 
                        outtweets.append([remove_and_put_sign(date), remove_and_put_sign(screen_name), remove_and_put_sign(title)])

        with open('tweets0430.csv', 'a') as f:
                writer = csv.writer(f)
                writer.writerows(outtweets)
        pass

def remove_and_put_sign(text):
        text = text.replace("\n", "")
        text = text.replace("\t", "")
        sign = '//0'
        if sign in text:
                text = text.replace(sign, "")
        return str(text) + sign

if __name__ == '__main__':
        get_all_tweets('realdonaldtrump')
        get_all_tweets('CNN')
        get_all_tweets('FoxNews')
        get_all_tweets('WashingtonPost')
        get_all_tweets('NYT')
        get_all_tweets('WSJ')
        get_all_tweets('NBC')
        get_all_tweets('ABC')
        get_all_tweets('TheBlaze')
        get_all_tweets('HuffPost')
        get_all_tweets('amthinker')
        get_all_tweets('bpolitics')
