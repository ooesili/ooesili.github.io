+++
tags = [
  'javascript',
  'react',
  'redux'
]
date = "2016-12-06T00:46:57-07:00"
title = "Redux: Eliminating the Three-File Feature"
draft = false

+++

> "Why do I have to touch three files to get a simple feature
> working?

&mdash; Dan Abramov - [You Might Not Need Redux][might-not-need]

This is a common complaint from those who are new to [Redux][redux]. Redux
provides a slew of benefits including, but not limited to, centralized state
management and the ability to use pure functions for state changes.  However,
like all useful things, it adds indirection to your code. The Redux community
sees this as a win and accepts the consequences, as do I, but I don't believe
that the infamous three-file feature is a necessary evil.

## Status Quo

This difficulty in adding an action comes from the design pattern that Redux's
official documentation uses. Let's look at how this pattern came about, then
we'll look at how we can improve on it. The docs demonstrate using
`switch/case` statements when creating reducers.

```javascript
// action types
const INCREMENT = 'INCREMENT'
const DECREMENT = 'DECREMENT'

// action creators
export const increment = createAction(INCREMENT)
export const decrement = createAction(DECREMENT)

// reducer
export function app (state = 0, action) {
  switch(action.type) {
    case INCREMENT:
      return state + 1
    case DECREMENT:
      return state - 1
    default:
      return state
  }
}
```

Even to someone unfamiliar with Redux, this is arguably quite clear. This
pattern works great for small applications and introductory documentation, but
it does not scale well, or at least it could scale better. This file will grow
and grow until the only way to find anything in your application is to use
`grep/ag` or your editor's search feature.

You don't have to be an expert programmer to know that keeping your entire
application in a single file is a bad idea. With that in mind, the logical next
step is to break up each section above into it's own file.

`actionTypes.js`
```javascript
export const INCREMENT = 'INCREMENT'
export const DECREMENT = 'DECREMENT'
```

`actions.js`
```javascript
export const increment = createAction(INCREMENT)
export const decrement = createAction(DECREMENT)
```

`reducer.js`
```javascript
export function app (state = 0, action) {
  switch(action.type) {
    case INCREMENT:
      return state + 1
    case DECREMENT:
      return state - 1
    default:
      return state
  }
}
```

You can also re-export these files from an `index` file if you didn't want to
change the way the module is consumed:

`index.js`
```javascript
export * from './reducer'
export * from './actions'
```

Wonderful! Now it's much easier to find what you are looking for. Where is the
`increment` action creators defined? `actions.js`. Looking for the increment
action handler? `reducer.js`.

What if we wanted to add an action to reset the state to `0`? Well, we'd of
course have to add a new type to `actionTypes.js`. Then we'd need to add an
action creator to `actions.js`. Finally we'd have to add another case for this
action in `reducer.js`. Behold, the three-file-feature!

Still, we're not yet to the point where adding a feature is painful. Even
though our module is split across three files, it's still easy to find things.
Yet again, pain comes when the program grows. At this point two choices present
themselves.

## Folders-by-feature vs Folders-by-type

When an app grows, there are generally two ways to organize its modules: by
putting modules of the same type together, or by putting modules of the same
feature together. This is a topic that deserves its own nuanced discussion so
we won't go too deep into it here, but we will briefly explore each option.

### Folders-by-type

Folders-by-type is something that will feel familiar to you if you have used
Ruby on Rails before. It's also a natural extension of the design pattern we've
already been looking at. The basic ideas is to center your folder structure
around the types of modules in your application.

```bash
blog/
  actionTypes/
    comments.js
    index.js
    posts.js
    users.js
  actions/
    comments.js
    index.js
    posts.js
    users.js
  reducers/
    comments.js
    index.js
    posts.js
    users.js
```

Now we can start to see the pain of our original design. Adding an action to
delete comments would involve touching at least 3 different files in 3
different folders. As the application grows further, and each of these folders
starts growing, we end up with 3 tree-like structures running parallel to each
other throughout our codebase.

Keeping these 3 structures in sync requires an unfortunate amount of
discipline. The declarations in each file should be sorted the same way for the
sake of consistency. This requires concious and continual effort as code is
added and modified, [since codebases naturally tend towards disorder over
time][entropy]. Working with these modules involves hurdling all three of these
trees, keeping more of the codebase than necessary in our heads while we work.

### Folders-by-feature

It seems [generally accepted][redux-by-feature] by the Redux community ([and
others][angular-by-feature]) that organizing modules by feature results in a
more maintainable and easier to navigate codebase. Let's take a look.

```bash
blog/
  comments/
    actions.js
    actionTypes.js
    index.js
    reducer.js
  posts/
    actions.js
    actionTypes.js
    index.js
    reducer.js
  users/
    actions.js
    actionTypes.js
    index.js
    reducer.js
```

When adding a feature, we still have to modify three different files, but at
least they are in the same folder. We can take it one step further by following
the ["ducks"][ducks] design pattern and combining each feature into a single
file.

```bash
blog/
  comments/
    index.js
  posts/
    index.js
  users/
    index.js
```

This feels like a step backwards, but if we keep our modules small and focused,
we'll have a flexible hierarchy of self-contained modules that can be freely
nested and moved around in a way that makes sense to our domain. This looks
great until we take a closer look at one of these modules. Our ducks are living
a double life!

```javascript
// actions
const ADD_USER = 'users/ADD_USER'
// ...

// action creators
export const addUser = createAction(ADD_USER)
// ...

// reducers
export function users (state = [], action) {
  switch (action.type) {
    case ADD_USER:
      // ...
    default:
      return state
  }
}
```

We're back in by-type land! We have dissonance between our high-level module
structure, and the structure of our code inside of each module. At first
glance, this seems unavoidable, and again if modules are kept small and focused
it's not a huge problem. However, it doesn't have to be this way. There is
still room for improvement.

Before we try to push forward, we need a direction. What is the exact problem
that we are trying to solve? What is the ideal behaviour of our system?
Personally, I want adding an action to only involve touching one file, and
ideally only a single block of code in that file.

## The Promised Land

As a first step, it would be really nice if we could group our actions and
action creators together. Easy, right?

```javascript
// actions
const INCREMENT = 'INCREMENT'
export const increment = createAction(INCREMENT)

const DECREMENT = 'DECREMENT'
export const decrement = createAction(DECREMENT)

// reducer
export function app (state = 0, action) {
  switch(action.type) {
    case INCREMENT:
      return state + 1
    case DECREMENT:
      return state - 1
    default:
      return state
  }
}
```

Adding an action now only involves touching two parts of this file. This is
definitely an improvement from before, but we are not yet down to the single
block of code. We want each of the `case` statements in our reducer to live
next to their actions. It would be impossible (and a bad idea) to sprinkle top
level declarations inside of a `switch/case` statement, but what if we could do
the opposite? Could we split our `switch` statement into small pieces, and then
put those pieces next to their actions?

```javascript
const reducerMap = {}

const INCREMENT = 'INCREMENT'
export const increment = createAction(INCREMENT)
reducerMap[INCREMENT] = (state) => state + 1

const DECREMENT = 'DECREMENT'
export const decrement = createAction(DECREMENT)
reducerMap[INCREMENT] = (state) => state - 1

export const app = combineActions(reducerMap, 0)
```

Voilà! Using the popular [redux-actions][redux-actions] library, we can split
up our reducer in exactly the way that we wanted. Each case statement becomes a
property of the `reducerMap` object, then a reducer is created and exported at
the end of the file using `combineActions`. Just to be sure, let's see what
it's like to add the aforementioned `RESET` action.

```javascript
const reducerMap = {}

const INCREMENT = 'INCREMENT'
export const increment = createAction(INCREMENT)
reducerMap[INCREMENT] = (state) => state + 1

const DECREMENT = 'DECREMENT'
export const decrement = createAction(DECREMENT)
reducerMap[DECREMENT] = (state) => state - 1

// just one block!
const RESET = 'RESET'
export const reset = createAction(RESET)
reducerMap[RESET] = (state) => 0

export const app = combineActions(reducerMap, 0)
```

Each action now lives in a single block of code which makes adding, modifying,
and removing actions very easy. It's also very easy to move actions between
modules when refactoring, and even easier to spot when one of the three parts
of an action is missing.

## Further Exploration

Although I see multiple advantages to this pattern, there are still unanswered
questions. For example, when using the popular [redux-thunk][redux-thunk]
middleware for performing side-effects, it becomes unclear where these
asynchronous actions should live. Since these actions don't have a direct
correlation with action handlers in reducer, it becomes hard to chose which
block of code to put their definitions next to.

Similarly, I am not sure how this pattern would play with
[redux-saga][redux-saga], since I have not used it before. I would love to see
some experimentation here.

[might-not-need]: https://medium.com/@dan_abramov/you-might-not-need-redux-be46360cf367#.wb2vtkxfr
[redux]: http://redux.js.org
[entropy]: https://en.wikipedia.org/wiki/Software_entropy
[redux-by-feature]: http://marmelab.com/blog/2015/12/17/react-directory-structure.html
[angular-by-feature]: https://github.com/johnpapa/angular-styleguide/blob/master/a1/README.md#folders-by-feature-structure
[ducks]: https://github.com/erikras/ducks-modular-redux
[redux-actions]: https://github.com/acdlite/redux-actions
[redux-thunk]: https://github.com/gaearon/redux-thunk
[redux-saga]: https://github.com/yelouafi/redux-saga
