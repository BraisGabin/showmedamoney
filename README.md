# Showmedamoney

This is an interview test I did in December 2018. I updated it a bit in October 2019.

## The task

The tas was to create an app to share money between friends. It should have three steps:

- Select contacts: A list of contacts where you should pick one or more of them to be able to go to the next page.
  - The data to fill this list is the characters that the Marbel api provides and the contacts of your mobile phone combined.
- The second one should be a screen where you can set the amount of money
- The third was an end screen where you see the amount of money, the list of selected contacts and how much money should they pay you.

## The architecture

I developed this test to try some new technologies:
- **Fragments**: I usually work with custom views
- **`ModelView`**: I usually work with custom views so I can't use them. Note: I used the androidx component `ModelView`. But I didn't use
anything related with MVVM.
- **Model View Intent**: At that moment I was starting with MVI and some frameworks. For this test I decided to implement the MVI
by myself from scratch. After building this I decided to use MVICore in 21Buttons. Handling the previous emitted state was kind
of messy in my implementation.

## Disclaimer

This is not how I build production apps. This app doesn't have a stable base where I could build thousands of lines of code.
This is just a probe of concept to solve a given problem. In a real production app I would add androidTests, I would improve the
Dagger injection, I would use a more standard way of MVI, etc.

For example: I didn't handle the permissions request in a nice way. To do so I would need to write a lot of lines of code that probes
nothing by my point of view. If you want to check how I implement proper UX you can [download the 21Buttons app][21buttons-app].


  [21buttons-app]: https://play.google.com/store/apps/details?id=com.android21buttons
