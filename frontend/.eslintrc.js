/* eslint-disable no-undef */
module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: ["eslint:recommended", "plugin:react/recommended", "prettier"],
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: "latest",
    sourceType: "module",
  },
  plugins: ["react", "prettier"],
  rules: {
    "no-var": "error",
    "no-console": "warn",
    "prefer-arrow-callback": "warn",
    "max-len": [
      "error",
      {
        code: 80,
        comments: 80,
        ignoreUrls: true,
      },
    ],
    "react/react-in-jsx-scope": "off",
    "react/prop-types": "off",
    "prettier/prettier": "error",
  },
};
