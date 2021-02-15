package app

class MyService {

    def demoIssue() {

        MyDomain.withSession { session ->

            def myDomain = MyDomain.get(1L)

            MyDomain.withTransaction {
                if (!myDomain) {
                    myDomain = new MyDomain(domainValue: 123, myEmbedded: new MyEmbedded(value: 123)).save()
                }
            }

            MyDomain.withTransaction {

                assert (myDomain != null)
                assert (!myDomain.isDirty())

                myDomain.domainValue = 456
                assert (myDomain.isDirty())

                myDomain.save()
            }

            assert (!myDomain.isDirty())
            session.flush() // As expected this does not result in an Update

            MyDomain.withTransaction {

                assert (myDomain != null)
                assert (!myDomain.isDirty())

                myDomain.myEmbedded.value = 789
                assert (myDomain.isDirty())

                myDomain.save()
            }

            MyDomain.withTransaction {
                assert (myDomain != null)
                assert (!myDomain.isDirty())
            } // Issue: This results is an Update to MyDomain when it is not dirty!

            assert (!myDomain.isDirty())
            session.flush() // Issue: Again this results is an Update to MyDomain when it is not dirty!

            assert (!myDomain.isDirty())
            session.flush() // Issue: Again this results is an Update to MyDomain when it still is not dirty!
        }
    }
}
