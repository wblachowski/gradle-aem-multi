const timeout = 5000;
const argv = require('yargs').argv;
const envData = require('../config/env.json');

console.log(argv.env);
console.log(envData[argv.env]);

describe(
    '/ (Home Page)',
    () => {
        console.log(process.argv);
        let page;
        beforeAll(async () => {
            page = await global.__BROWSER__.newPage();
            await page.goto(envData['local-publish'])
        }, timeout);

        afterAll(async () => {
            await page.close()
        });

        it('should load without error', async () => {
            let text = await page.evaluate(() => document.body.textContent);
            expect(text).toContain('google')
        })
    },
    timeout
);
