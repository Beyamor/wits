(ns wits.projects.backends.message-in-a-bottle
  (:use [compojure.core :only [GET defroutes]]
        [wits.data :as data]
        korma.core
        [korma.db :only [with-db]]))

(defentity messages
           (table :miab_messages))

(def all-messages (-> (select* messages) (where {:seen false})))

(defn get-other-messages
  [ip]
  (-> all-messages
    (where (not= ip :ip))
    select))

(defn insert-message
  [ip message]
  (insert messages
          (values {:ip ip :message message})))

(declare fortunes) 

(defn trade-messages
  [ip message]
  (with-db data/wits-db
           (insert-message ip message)
           (let [other-messages (get-other-messages ip)]
             (if-not (empty? other-messages)
               (let [other-message (rand-nth other-messages)]
                 (update messages
                         (set-fields {:seen true})
                         (where {:id (:id other-message)}))
                 (:message other-message))
               (rand-nth fortunes)))))

(defroutes all-routes
           (GET "/projects/message-in-a-bottle/submit"
                {{:keys [message]} :params :keys [remote-addr headers]}
                (let [ip (or (get headers "x-forwarded-for")
                             remote-addr)]
			(if-not (empty? message)
			  (trade-messages ip message)
			  {:status 400 :body "Dude, I dunno."}))))

(def fortunes
  ["“Welcome” is a powerful word."
   "A dubious friend may be an enemy in camouflage."
   "A feather in the hand is better than a bird in the air."
   "A fresh start will put you on your way."
   "A friend asks only for your time not your money."
   "A friend is a present you give yourself."
   "A gambler not only will lose what he has, but also will lose what he doesn’t have."
   "A golden egg of opportunity falls into your lap this month."
   "A good time to finish up old tasks."
   "A hunch is creativity trying to tell you something."
   "A light heart carries you through all the hard times."
   "A new perspective will come with the new year."
   "A person is never to old to learn."
   "A person of words and not deeds is like a garden full of weeds."
   "A pleasant surprise is waiting for you."
   "A smile is your personal welcome mat."
   "A smooth long journey! Great expectations."
   "A soft voice may be awfully persuasive."
   "A truly rich life contains love and art in abundance."
   "Accept something that you cannot change, and you will feel better."
   "Adventure can be real happiness."
   "Advice is like kissing.  It costs nothing and is a pleasant thing to do."
   "Advice, when most needed, is least heeded."
   "All the effort you are making will ultimately pay off."
   "All the troubles you have will pass away very quickly."
   "All will go well with your new project."
   "All your hard work will soon pay off."
   "Allow compassion to guide your decisions."
   "An agreeable romance might begin to take on the appearance."
   "An important person will offer you support."
   "An inch of time is an inch of gold."
   "Be careful or you could fall for some tricks today."
   "Beauty in its various forms appeals to you."
   "Because you demand more from yourself, others respect you deeply."
   "Believe in yourself and others will too."
   "Believe it can be done."
   "Better ask twice than lose yourself once."
   "Carve your name on your heart and not on marble."
   "Change is happening in your life, so go with the flow!"
   "Competence like yours is underrated."
   "Congratulations!  You are on your way."
   "Could I get some directions to your heart?"
   "Courtesy begins in the home."
   "Courtesy is contagious."
   "Curiosity kills boredom.  Nothing can kill curiosity."
   "Dedicate yourself with a calm mind to the task at hand."
   "Depart not from the path which fate has you assigned."
   "Determination is what you need now."
   "Disbelief destroys the magic."
   "Distance yourself from the vain."
   "Do not be intimidated by the eloquence of others."
   "Do not let ambitions overshadow small success."
   "Do not make extra work for yourself."
   "Do not underestimate yourself. Human beings have unlimited potentials."
   "Don’t be discouraged, because every wrong attempt discarded is another step forward."
   "Don’t confuse recklessness with confidence."
   "Don’t just spend time.  Invest it."
   "Don’t just think, act!"
   "Don’t let friends impose on you, work calmly and silently."
   "Don’t let the past and useless detail choke your existence."
   "Don’t let your limitations overshadow your talents."
   "Don’t worry; prosperity will knock on your door soon."
   "Each day, compel yourself to do something you would rather not do."
   "Education is the ability to meet life’s situations."
   "Emulate what you admire in your parents."
   "Emulate what you respect in your friends."
   "Every flower blooms in its own sweet time."
   "Every wise man started out by asking many questions."
   "Everyday in your life is a special occasion."
   "Failure is the chance to do better next time."
   "Feeding a cow with roses does not get extra appreciation."
   "For hate is never conquered by hate.  Hate is conquered by love."
   "Fortune Not Found: Abort, Retry, Ignore?"
   "From listening comes wisdom and from speaking repentance."
   "From now on your kindness will lead you to success."
   "Get your mind set — confidence will lead you on."
   "Get your mind set…confidence will lead you on."
   "Go take a rest; you deserve it."
   "Good news will be brought to you by mail."
   "Good news will come to you by mail."
   "Good to begin well, better to end well."
   "Happiness begins with facing life with a smile and a wink."
   "Happiness will bring you good luck."
   "Happy life is just in front of you."
   "Hard words break no bones, fine words butter no parsnips."
   "Have a beautiful day."
   "He who expects no gratyou."
   "The one that recognizes the illusion does not act as if it is real."
   "The only people who never fail are those who never try."
   "The person who will not stand for something will fall for anything."
   "The philosophy of one century is the common sense of the next."
   "The saints are the sinners who keep on trying."
   "The secret to good friends is no secret to you."
   "The small courtesies sweeten life, the greater ennoble it."
   "The smart thing to do is to begin trusting your intuitions."
   "The strong person understands how to withstand substantial loss."
   "The sure way to predict the future is to invent it."
   "The truly generous share, even with the undeserving."
   "The value lies not within any particular thing, but in the desire placed on that thing."
   "The weather is wonderful."
   "There is no mistake so great as that of being always right."
   "There is no wisdom greater than kindness."
   "There is not greater pleasure than seeing your lived ones prosper."
   "There’s no such thing as an ordinary cat."
   "Things don’t just happen; they happen just."
   "Those who care will make the effort."
   "Time and patience are called for many surprises await you!."
   "Time is precious, but truth is more precious than time"
   "To know oneself, one should assert oneself."
   "Today is the conserve yourself, as things just won’t budge."
   "Today, your mouth might be movingten you."
   "Your happiness is before you, not behind you!  Cherish it."
   "Your hard work will payoff today."
   "Your heart will always make itself known through your words."
   "Your home is the center of great love."
   "Your ideals are well within your reach."
   "Your infinite capacity for patience will be rewarded sooner or later."
   "Your leadership qualities will be tested and proven."
   "Your life will be happy and peaceful."
   "Your life will get more and more exciting."
   "Your love life will be happy and harmonious."
   "Your love of music will be an important part of your life."
   "Your loyalty is a virtue, but not when it’s wedded with blind stubbornness."
   "Your mind is creative, original and alert."
   "Your mind is your greatest asset."
   "Your quick wits will get you out of a tough situation."
   "Your success will astonish everyone."
   "Your talents will be recognized and suitably rewarded."
   "Your work interests can capture the highest status or prestige."])
